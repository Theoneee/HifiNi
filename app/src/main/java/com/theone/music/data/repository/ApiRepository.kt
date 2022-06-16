package com.theone.music.data.repository

import com.theone.music.app.ext.md5encode
import com.theone.music.data.model.*
import com.theone.music.net.NetConstant
import rxhttp.toClass
import rxhttp.toStr
import rxhttp.wrapper.cahce.CacheMode
import rxhttp.wrapper.param.RxHttp
import rxhttp.wrapper.param.toResponse
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
            .addHeader("content-type", "application/x-www-form-urlencoded; charset=UTF-8")
            .addHeader("x-requested-with", "XMLHttpRequest")
            .add("email", email)
            .add("password", password)
            .toResponse()


    /**
     * 注册
     * @param email String
     * @param username String
     * @param password String 密码,使用MD5加密过的
     * @param repeat_password String 确认密码，原始密码
     * @return String
     */
    fun register(email: String, username: String, password: String) =
        RxHttp.postForm(NetConstant.REGISTER)
            .addHeader("content-type", "application/x-www-form-urlencoded; charset=UTF-8")
            .addHeader("x-requested-with", "XMLHttpRequest")
            .add("email", email)
            .add("username", username)
            .add("password", password.md5encode())
            .add("repeat_password", password)
            .toResponse()


    /**
     * 签到 - 已登录情况下
     * @return Await<Response>
     */
    fun sign() = RxHttp.postForm(NetConstant.SIGN)
        .addHeader("content-type", "application/x-www-form-urlencoded; charset=UTF-8")
        .addHeader("x-requested-with", "XMLHttpRequest")
        .toResponse()

    /**
     * 用户信息
     * @return Await<String>
     */
    fun userInfo() = RxHttp.get(NetConstant.USER_INFO)
        .addHeader("content-type", "application/x-www-form-urlencoded; charset=UTF-8")
        .addHeader("x-requested-with", "XMLHttpRequest")
        .toClass<String>()


    /**
     * https://hifini.com/
     * url = forum-15.htm
     */
    suspend fun request(url: String, vararg formatArgs: Any): String {
        return RxHttp.get(url, *formatArgs)
            .setCacheMode(CacheMode.ONLY_NETWORK)
            .toStr()
            .await()
    }

}