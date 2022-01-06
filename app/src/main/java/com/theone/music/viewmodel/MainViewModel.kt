package com.theone.music.viewmodel

import com.theone.common.ext.logE
import com.theone.music.app.ext.URLEncode
import com.theone.music.data.model.Music
import com.theone.music.data.model.MusicInfo
import com.theone.music.data.repository.DataRepository
import com.theone.music.net.NetConstant
import com.theone.mvvm.core.base.viewmodel.BaseListViewModel

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
 * @date 2022-01-04 11:25
 * @describe TODO
 * @email 625805189@qq.com
 * @remark
 */
class MainViewModel : BaseListViewModel<MusicInfo>() {

    var keyWord: String = ""


    override fun requestServer() {
        request({
            onSuccess(
                DataRepository.INSTANCE.get(
                    NetConstant.SEARCH,
                    keyWord.URLEncode()
                )
            )
        })
    }

}