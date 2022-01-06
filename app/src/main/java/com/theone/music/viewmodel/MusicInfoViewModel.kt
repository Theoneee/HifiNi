package com.theone.music.viewmodel

import androidx.databinding.ObservableBoolean
import com.theone.music.data.model.MusicInfo
import com.theone.music.data.repository.DataRepository
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
class MusicInfoViewModel:BaseRequestViewModel<MusicInfo>() {

    val isPlaying = ObservableBoolean()
    var cover:StringObservableField = StringObservableField()
    var link:String = ""

    override fun requestServer() {
        request({
            onSuccess(DataRepository.INSTANCE.getMusicInfo(link))
        })
    }

}