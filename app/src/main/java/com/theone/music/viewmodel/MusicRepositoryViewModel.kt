package com.theone.music.viewmodel

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
 * @date 2022-04-06 16:27
 * @describe TODO
 * @email 625805189@qq.com
 * @remark
 */
class MusicRepositoryViewModel:BaseListViewModel<Music>() {

    override fun requestServer() {
        request({
            onSuccess(DataRepository.INSTANCE.get("https://www.hifini.com/index.htm"))
        })
    }

}