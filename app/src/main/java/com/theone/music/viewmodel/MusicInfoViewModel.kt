package com.theone.music.viewmodel

import androidx.lifecycle.viewModelScope
import com.kunminx.player.bean.dto.PlayingMusic
import com.theone.music.data.repository.DataRepository
import com.theone.common.callback.databind.BooleanObservableField
import com.theone.common.callback.databind.IntObservableField
import com.theone.common.callback.databind.StringObservableField
import com.theone.music.app.util.CacheUtil
import com.theone.music.data.model.CollectionEvent
import com.theone.music.data.model.Music
import com.theone.music.data.model.User
import com.theone.music.player.PlayerManager
import com.theone.mvvm.core.base.viewmodel.BaseRequestViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

//  ┏┓　　　┏┓
//┏┛┻━━━┛┻┓
//┃　　　　　　　┃
//┃　　　━　　　┃
//┃　┳┛　┗┳　┃
//┃　　　　　　　┃
//┃　　　┻　　　┃
//┃　　　　　　　┃
//┗━┓　　　┏━┛
//    ┃　　　┃                  神兽保佑
//    ┃　　　┃                  永无BUG！
//    ┃　　　┗━━━┓
//    ┃　　　　　　　┣┓
//    ┃　　　　　　　┏┛
//    ┗┓┓┏━┳┓┏┛
//      ┃┫┫　┃┫┫
//      ┗┻┛　┗┻┛
/**
 * @author The one
 * @date 2022-01-04 15:26
 * @describe TODO
 * @email 625805189@qq.com
 * @remark
 */
class MusicInfoViewModel : BaseRequestViewModel<Music>() {

    /**
     * 数据是否成功
     * 用于控制界面是否显示
     */
    val isSuccess = BooleanObservableField()

    /**
     * 音乐数据是否设置成功
     * 用于控制是否显示回调的音乐播放信息
     */
    val isSetSuccess = BooleanObservableField()

    val max = IntObservableField()
    val progress = IntObservableField()
    val name = StringObservableField()
    val author = StringObservableField()
    val nowTime = StringObservableField("00:00")
    val allTime = StringObservableField("00:00")
    val isCollection = BooleanObservableField()
    val isCollectionEnable = BooleanObservableField()
    val isPlaying = BooleanObservableField()
    var cover: StringObservableField = StringObservableField()
    var link: String = ""

    init {
        isCollectionEnable.set(CacheUtil.isLogin())
    }

    var isReload = false

    override fun requestServer() {
        request({
            val music = DataRepository.INSTANCE.getMusicInfo(link, isReload)
            onSuccess(music)
        })
    }

    fun requestDbMusic(): Music? {
        return DataRepository.INSTANCE.getDbMusicInfo(link)
    }

    fun requestCollection(user: User?, url: String) {
        user?.let {
            request({
                isCollection.set(
                    DataRepository.MUSIC_DAO.getUserCollectionMusic(it.id, url).isNotEmpty()
                )
            })
        }
    }

    fun toggleCollection(userId: Int, collectionEvent: CollectionEvent) {
        with(collectionEvent) {
            viewModelScope.launch {
                DataRepository.MUSIC_DAO.updateCollectionMusic(
                    userId,
                    music.shareUrl,
                    if (isCollection) 1 else 0,
                    System.currentTimeMillis()
                )
            }
        }
    }

    private fun updateMusicLastPlayDate() {
        val time = System.currentTimeMillis()
        DataRepository.MUSIC_DAO.updateMusicLastPlayDate(time, link)
    }

    fun setMediaCourse(music: PlayingMusic<*,*,*>){
        // 只有在设置了音乐数据后才能设置播放信息，避免被上一首的播放信息污染
        if(!isSetSuccess.get()){
            return
        }
        max.set(music.duration)
        progress.set(music.playerPosition)
        nowTime.set(music.nowTime)
        allTime.set(music.allTime)
    }

    fun setMusicInfo(music: Music, user: User?) {
        isSuccess.set(true)
        isCollectionEnable.set(user != null)
        name.set(music.title)
        author.set(music.author)
        cover.set(music.pic)
        requestCollection(user, music.shareUrl)
    }

    fun setMediaSource(data: Music, user: User?=null,newData: Boolean = false) {
        setMusicInfo(data,user)
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                // 不是新数据才检查地址是否可行
                if (!newData && DataRepository.INSTANCE.checkUrl(data.getMusicUrl())) {
                    isReload = true
                    requestServer()
                    return@withContext
                }
                val album = DataRepository.INSTANCE.createAlbum(data)
                PlayerManager.getInstance().loadAlbum(album, 0)
                isSetSuccess.set(true)
                // 更新音乐信息 最后播放时间
                updateMusicLastPlayDate()
            }
        }
    }

    fun reset() {
        isSuccess.set(false)
        isSetSuccess.set(false)
        isCollection.set(false)
        isCollectionEnable.set(false)
        isPlaying.set(false)
        max.set(0)
        progress.set(0)
        nowTime.set("00:00")
        allTime.set("00:00")
    }

}