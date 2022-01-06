package com.theone.music.app.ext

import com.theone.common.ext.toHtml
import com.theone.mvvm.core.util.FileDirectoryUtil
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


fun writeStringToFile(path: String = FileDirectoryUtil.getCachePath()+ File.separator+"temp.txt", content: String) {
    try {
        val pw = PrintWriter(FileWriter(path))
        pw.print(content)
        pw.close()
    } catch (e: IOException) {
        e.printStackTrace()
    }
}

fun String.URLEncode(): String {
    return java.net.URLEncoder.encode(this, "utf8").run {
        replace("%", "_")
    }
}

fun String.fullSize():String{
    if(contains("?")){
        return substring(0,indexOf("?"))
    }
    return this
}

fun String.getHtmlString():CharSequence{
    if(contains("span")){
        return toHtml()
    }
    return this
}

fun String.splitMusicInfo(r1:String, r2:String):List<String>{
    val result = mutableListOf<String>()
    if(contains(r1) && contains(r2)){
        val index  = indexOf(r1)
        result[0] = substring(0,index)
        result[1] = substring(index+1,indexOf(r2))
    }
    return result
}