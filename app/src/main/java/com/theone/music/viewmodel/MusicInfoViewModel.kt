package com.theone.music.viewmodel

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.theone.music.data.model.CollectionEvent
import com.theone.music.data.model.Music
import com.theone.music.data.repository.DataRepository
import com.theone.common.callback.databind.BooleanObservableField
import com.theone.common.callback.databind.IntObservableField
import com.theone.common.callback.databind.StringObservableField
import com.theone.music.app.util.CacheUtil
import com.theone.music.data.repository.LrcRepository
import com.theone.mvvm.base.viewmodel.BaseViewModel
import com.theone.mvvm.core.base.viewmodel.BaseRequestViewModel
import kotlinx.coroutines.launch

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
class MusicInfoViewModel : BaseViewModel() {

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

    fun checkUrl(url: String) = DataRepository.INSTANCE.checkUrl(url)

    fun requestDbMusic(): Music? {
        return DataRepository.INSTANCE.getDbMusicInfo(link)
    }

    fun requestCollection(username: String, url: String) {
        isCollection.set(DataRepository.MUSIC_DAO.findCollectionMusics(username, url).isNotEmpty())
    }

    fun toggleCollection(username: String, collectionEvent: CollectionEvent) {
        with(collectionEvent) {
            viewModelScope.launch {
                DataRepository.MUSIC_DAO.updateCollectionMusic(
                    username,
                    music.shareUrl,
                    if (collection) 1 else 0,
                    System.currentTimeMillis()
                )
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