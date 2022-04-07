package com.theone.music.viewmodel

import com.theone.music.app.ext.URLEncode
import com.theone.music.data.model.Music
import com.theone.music.data.model.PageInfo
import com.theone.music.data.repository.DataRepository
import com.theone.music.net.NetConstant
import com.theone.mvvm.core.base.viewmodel.BaseListViewModel
import com.theone.mvvm.core.data.net.IPageInfo

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
 * @date 2022-04-07 08:42
 * @describe TODO
 * @email 625805189@qq.com
 * @remark
 */
class SingerSearchViewModel:BaseListViewModel<Music>() {

    var type:Int = 0

    override fun requestServer() {
        request({
            DataRepository.INSTANCE.getSingerList(NetConstant.TAG,type,page).run {
                onSuccess(list, PageInfo(page,totalPage))
            }
        })
    }

}