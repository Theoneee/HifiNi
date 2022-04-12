package com.theone.music.viewmodel

import com.theone.music.data.repository.DataRepository
import com.theone.music.data.constant.NetConstant
import com.theone.music.data.model.Music
import com.theone.music.data.model.PageInfo
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
 * @date 2022-04-06 16:27
 * @describe TODO
 * @email 625805189@qq.com
 * @remark
 */
class MusicRepositoryViewModel:BaseListViewModel<Music>() {

    override fun requestServer() {
        request({
            DataRepository.INSTANCE.get(NetConstant.INDEX,page).run {
                onSuccess(list, PageInfo(page,totalPage))
            }
        })
    }

}