package com.theone.music.data.model

import com.theone.common.callback.IImageUrl
import com.theone.music.net.NetConstant

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
 * @date 2022-01-04 16:07
 * @describe TODO
 * @email 625805189@qq.com
 * @remark
 */
data class MusicInfo(var title: String, var author: String, var url: String, var pic: String):
    IImageUrl {

    fun getMusicUrl():String {
        return if (url.startsWith("http")) {
            url
        } else {
            NetConstant.BASE_URL + url
        }
    }

    override fun getHeight(): Int  = 0

    override fun getImageUrl(): String = getMusicUrl()

    override fun getRefer(): String? = null

    override fun getThumbnail(): String?  = null

    override fun getWidth(): Int = 0

    override fun isVideo(): Boolean  = false

}