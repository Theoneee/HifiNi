package com.theone.music.viewmodel

import androidx.databinding.ObservableBoolean
import com.theone.music.data.model.CollectionEvent
import com.theone.music.data.model.Music
import com.theone.music.data.model.TestAlbum
import com.theone.music.data.repository.DataRepository
import com.theone.mvvm.callback.databind.IntObservableField
import com.theone.mvvm.callback.databind.StringObservableField
import com.theone.mvvm.core.base.viewmodel.BaseRequestViewModel

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

    val max = IntObservableField()
    val progress = IntObservableField()

    val name = StringObservableField()
    val author = StringObservableField()
    val nowTime = StringObservableField()
    val allTime = StringObservableField()

    val isSuccess = ObservableBoolean(false)
    val isCollection = ObservableBoolean(false)
    val isPlaying = ObservableBoolean()
    var cover: StringObservableField = StringObservableField()
    var link: String = ""

    override fun requestServer() {
        request({
            onSuccess(DataRepository.INSTANCE.getMusicInfo(link))
        })
    }

    fun requestCollection(url: String) {
        request({
            isCollection.set(DataRepository.MUSIC_DAO.findMusics(url).isNotEmpty())
        })
    }

    fun toggleCollection(collectionEvent: CollectionEvent) {
        with(collectionEvent){
            if (collection) {
                music.createDate = System.currentTimeMillis()
                DataRepository.MUSIC_DAO.insert(arrayListOf(music))
            } else {
                DataRepository.MUSIC_DAO.delete(music.shareUrl)
            }
        }
    }

}