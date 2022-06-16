package com.theone.music.domain.request

import com.theone.music.data.model.Music
import com.theone.music.data.repository.DataRepository
import com.theone.mvvm.core.base.request.BaseRequest

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
 * @date 2022-06-15 09:28
 * @describe TODO
 * @email 625805189@qq.com
 * @remark
 */
class MusicPlayerRequest : BaseRequest<Music>() {

    var isReload = false

    suspend fun getMusicInfo(link:String) {
        val music = DataRepository.INSTANCE.getMusicInfo(link, isReload)
        onSuccess(music)
    }

}