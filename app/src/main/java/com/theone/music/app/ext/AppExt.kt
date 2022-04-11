package com.theone.music.app.ext

import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.theone.common.ext.startActivity
import com.theone.common.ext.toHtml
import com.theone.music.app.util.CacheUtil
import com.theone.music.data.model.Music
import com.theone.music.data.model.TestAlbum
import com.theone.music.ui.activity.LoginRegisterActivity
import com.theone.mvvm.base.fragment.BaseQMUIFragment
import com.theone.mvvm.core.app.util.FileDirectoryUtil
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.io.PrintWriter

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

fun TestAlbum.TestMusic.toMusic(): Music {
    return Music(title = title,author = author,url = url,pic = coverImg,shareUrl = shareUrl)
}
