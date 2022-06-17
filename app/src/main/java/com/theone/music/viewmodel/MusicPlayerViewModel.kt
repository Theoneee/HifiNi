package com.theone.music.viewmodel

import androidx.lifecycle.viewModelScope
import com.theone.music.data.model.Music
import com.theone.music.data.repository.DataRepository
import com.theone.music.domain.request.MusicPlayerRequest
import com.theone.mvvm.core.app.ext.request
import com.theone.mvvm.core.base.viewmodel.BaseRequestVM
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
class MusicPlayerViewModel:BaseRequestVM<MusicPlayerRequest>() {

    var link: String = ""
    var isReload = false

    override fun createRequest() = MusicPlayerRequest()

    override fun requestServer() {
        request({
            getRequest().getMusicInfo(link,isReload)
        })
    }

    fun requestDbMusic(): Music?{
        return DataRepository.INSTANCE.getDbMusicInfo(link)
    }

    fun updateMusicLastPlayDate(){
        val time = System.currentTimeMillis()
        DataRepository.MUSIC_DAO.updateMusicLastPlayDate(time,link)
    }

}