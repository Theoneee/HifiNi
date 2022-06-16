package com.theone.music.data.repository

import com.theone.common.ext.trimAll
import com.theone.music.data.model.UserInfo
import org.jsoup.Jsoup

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
 * @date 2022-06-14 11:05
 * @describe TODO
 * @email 625805189@qq.com
 * @remark
 */
class JsoupRepository {

    companion object {

        val TAG = "JsoupRepository"

        val INSTANCE: JsoupRepository by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            JsoupRepository()
        }

    }

    fun parseUserInfo(html: String) =
        Jsoup.parse(html).select("div.col-md-6").toString().run {
            val theme = substring("主题数：</span>", "<br>")
            val post = substring("帖子数：</span>", "<br>")
            val collection = substring("<a href=\"my-favorites.htm\" target=\"_blank\">", "</a>")
            val coin = substring("bolder;\">", "</em>")
            val group = substring("用户组：</span>", "<br>")
            val createDate = substring("创建时间：</span>", "<br>")
            val lastLoginDate = substring("最后登录：</span>", "<br>")
            val email = substring("Email：</span>", "</div>")
            UserInfo(theme, post, collection, coin, group, createDate, lastLoginDate, email)
        }

    private fun String.substring(start: String, end: String): String {
        val startIndex = indexOf(start) + start.length
        val endIndex = indexOf(end, startIndex)
        return substring(startIndex, endIndex).trimAll().replace("\n", "")
    }

}