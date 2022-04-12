package com.theone.music.app.ext

import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.theone.common.ext.toHtml
import com.theone.music.data.model.Music
import com.theone.music.data.model.TestAlbum

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
 * @date 2022-01-04 13:21
 * @describe TODO
 * @email 625805189@qq.com
 * @remark
 */

fun String.URLEncode(): String {
    return java.net.URLEncoder.encode(this, "utf8").run {
        replace("%", "_")
    }
}

fun String.getHtmlString():CharSequence{
    if(contains("span")){
        return toHtml()
    }
    return this
}

fun BaseQuickAdapter<*, *>.removeItem(position: Int){
    if (position != RecyclerView.NO_POSITION && data.size > position) {
        data.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, data.size)
    }
}
