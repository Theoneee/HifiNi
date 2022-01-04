package com.theone.music.app.ext

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