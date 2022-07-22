package com.theone.music.app.util

import com.google.gson.Gson
import com.theone.music.data.model.User
import com.theone.music.data.model.UserInfo
import com.theone.mvvm.core.app.util.MMKVUtil


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
 * @date 2021/3/11 0011
 * @describe TODO
 * @email 625805189@qq.com
 * @remark
 */
object CacheUtil {

    private const val USER: String = "user"
    private const val USER_INFO: String = "user_info"
    private const val FIRST: String = "user_first"

    fun isLogin(): Boolean = null != getUser()

    fun loginOut() {
        setUser(null)
        setUserInfo(null)
    }

    fun setUser(user: User?) {
        user.putClassBean(USER)
    }

    fun getUser(): User?  = USER.getClassBean()

    fun setUserInfo(userInfo: UserInfo?) {
        userInfo.putClassBean(USER_INFO)
    }

    fun getUserInfo(): UserInfo?  = USER_INFO.getClassBean()

    fun isFirst() = MMKVUtil.getBoolean(FIRST,true)

    fun setFirst(value:Boolean){
        MMKVUtil.putBoolean(FIRST,value)
    }

    private fun <T> T?.putClassBean(key:String){
        val json = if (null != this) Gson().toJson(this) else ""
        MMKVUtil.putString(key, json)
    }

    private inline fun <reified T> String.getClassBean():T?{
        val json = MMKVUtil.getString(this)
        return if (json.isNullOrEmpty())
            null
        else
            Gson().fromJson(json, T::class.java)
    }

}