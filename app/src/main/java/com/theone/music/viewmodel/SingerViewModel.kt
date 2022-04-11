package com.theone.music.viewmodel

import com.theone.music.data.model.Singer
import com.theone.music.data.repository.DataRepository
import com.theone.music.data.constant.NetConstant
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
 * @date 2022-04-07 09:10
 * @describe TODO
 * @email 625805189@qq.com
 * @remark
 */
class SingerViewModel:BaseListViewModel<Singer>() {

    override fun requestServer() {
        request({
            onSuccess(DataRepository.INSTANCE.getSingerList(NetConstant.BASE_URL))
        })
    }
}