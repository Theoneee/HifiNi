package com.theone.music.net

import rxhttp.wrapper.annotation.DefaultDomain

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
 * @date 2022-01-04 11:30
 * @describe TODO
 * @email 625805189@qq.com
 * @remark
 */
object NetConstant {

    @DefaultDomain
    const val BASE_URL = "https://www.hifini.com/"

    /**
     * 登录 - 拿取[cookie]里的[bbs_token]
     */
    const val LOGIN = "user-login.htm"

    /**
     * 注册
     * @POST
     * @param email
     * @param username
     * @param password
     * @param repeat_password
     */
    const val REGISTER = "user-create.htm"

    /**
     * 修改头像
     * @POST
     * @param width
     * @param height
     * @param action
     * @param filetype  jpg
     * @param name xxx.png
     * @param data file
     */
    const val AVATAR = "my-avatar.htm"

    /**
     * 签到
     */
    const val SIGN = "sg_sign.htm"

    /**
     * 用户信息
     */
    const val USER_INFO = "my.htm"


    const val INDEX = "index-%d.htm"

    const val FORUM = "forum-%d-%d.htm"

    const val SEARCH = "search-%s.htm"

    const val HOT = "index-0-2.htm"

    const val RANK = "index-0-%d.htm"

    const val TAG = "tag-%d-%d.htm"

    val CATEGORY = mapOf<String,Int>("华语" to 1,"日韩" to 15,"欧美" to 10,"Remix" to 11,"纯音乐" to 12,"异次元" to 13)

    val RANK_TYPES = mapOf<String,Int>("日榜" to 5,"周榜" to 4,"月榜" to 3)

}