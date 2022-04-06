package com.theone.music.viewmodel

import androidx.lifecycle.viewModelScope
import com.theone.music.data.model.CollectionEvent
import com.theone.music.data.model.Music
import com.theone.music.data.repository.DataRepository
import com.theone.common.callback.databind.BooleanObservableField
import com.theone.common.callback.databind.IntObservableField
import com.theone.common.callback.databind.StringObservableField
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
    val isPlaying = BooleanObservableField()
    var cover: StringObservableField = StringObservableField()
    var link: String = ""

    var isReload = false

    override fun requestServer() {
        request({
            onSuccess(DataRepository.INSTANCE.getMusicInfo(link,isReload))
        })
    }

    fun requestCollection(url: String) {
        request({
            isCollection.set(DataRepository.MUSIC_DAO.findCollectionMusics(url).isNotEmpty())
        })
    }

    fun toggleCollection(userId:Int,collectionEvent: CollectionEvent) {
        with(collectionEvent) {
            viewModelScope.launch {
                DataRepository.MUSIC_DAO.updateCollectionMusic(userId,music.shareUrl,if (collection) 1 else  0,System.currentTimeMillis())
            }
        }
    }

    fun reset(){
        isSuccess.set(false)
        isSetSuccess.set(false)
        isCollection.set(false)
        isPlaying.set(false)
        max.set(0)
        progress.set(0)
        nowTime.set("00:00")
        allTime.set("00:00")
    }

}