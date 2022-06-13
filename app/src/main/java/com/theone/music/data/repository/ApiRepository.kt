package com.theone.music.data.repository

import android.util.Log
import com.theone.common.ext.getNumbers
import com.theone.lover.data.room.AppDataBase
import com.theone.music.data.model.*
import com.theone.music.data.room.DownloadDao
import com.theone.music.data.room.MusicDao
import com.theone.music.data.room.UserDao
import com.theone.music.net.NetConstant
import com.theone.music.player.PlayerManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import rxhttp.toOkResponse
import rxhttp.toStr
import rxhttp.wrapper.cahce.CacheMode
import rxhttp.wrapper.param.RxHttp
import rxhttp.wrapper.utils.GsonUtil
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

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
 * @date 2022-01-04 11:29
 * @describe TODO
 * @email 625805189@qq.com
 * @remark
 */
class ApiRepository {

    companion object {

        val INSTANCE: ApiRepository by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            ApiRepository()
        }

    }

    /**
     * 用户登录
     * @param email String
     * @param password String
     * @return Await<Response>
     */
    fun login(email: String, password: String) =
        RxHttp.postForm(NetConstant.LOGIN)
        .add("email", email)
        .add("password", password)
        .toOkResponse()


    fun sign() = RxHttp.postForm(NetConstant.SIGN)
            .toOkResponse()

}