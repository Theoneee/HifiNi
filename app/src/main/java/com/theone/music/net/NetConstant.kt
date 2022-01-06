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

    const val INDEX = "index-%d-%d.htm"
    const val FORUM = "forum-%d-%d.htm"

    const val SEARCH = "search-%s.htm"

    val CATEGORY = mapOf<String,Int>("华语" to 1,"日韩" to 15,"欧美" to 10,"Remix" to 11,"纯音乐" to 12,"异次元" to 13)
//    val HOME_TYPES = mapOf<String,String>("最新" to "index-%d.htm","热门" to "index-0-2.htm","月榜" to "","周榜" to "","日榜" to "")

}