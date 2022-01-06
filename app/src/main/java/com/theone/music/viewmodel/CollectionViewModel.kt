package com.theone.music.viewmodel

import com.theone.lover.data.room.AppDataBase
import com.theone.music.data.model.MusicInfo
import com.theone.music.data.repository.DataRepository
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
 * @date 2022-01-06 13:43
 * @describe TODO
 * @email 625805189@qq.com
 * @remark
 */
class CollectionViewModel:BaseListViewModel<MusicInfo>() {

    override fun requestServer() {
       request({
           onSuccess(DataRepository.MUSIC_DAO.getMusicList())
       })
    }
}