package com.theone.music.data.model

import com.theone.common.ext.toHtml
import com.theone.music.app.ext.getHtmlString

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
 * @date 2022-01-04 14:08
 * @describe TODO
 * @email 625805189@qq.com
 * @remark
 */
data class Music(
    var author: String,
    var avatar: String,
    var name: String,
    var link: String
) {

    fun getAuthorHtml(): CharSequence{
        return author.getHtmlString()
    }

    fun getNameHtml(): CharSequence{
        return name.getHtmlString()
    }

}