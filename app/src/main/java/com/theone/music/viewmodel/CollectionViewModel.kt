package com.theone.music.viewmodel

import android.util.Log
import com.theone.common.ext.logI
import com.theone.music.data.model.Music
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
class CollectionViewModel:BaseListViewModel<Music>() {

    override fun requestServer() {
       request({
           onSuccess(DataRepository.MUSIC_DAO.getCollectionMusicList())
       })
    }
}