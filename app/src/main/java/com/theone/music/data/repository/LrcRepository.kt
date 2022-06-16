package com.theone.music.data.repository

import android.util.Log
import com.theone.music.data.model.*
import com.theone.music.net.LrcUrl
import com.theone.mvvm.base.appContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Response
import org.jsoup.Jsoup
import rxhttp.*
import rxhttp.wrapper.OkHttpCompat.url
import rxhttp.wrapper.coroutines.Await
import rxhttp.wrapper.param.RxHttp
import java.io.File
import java.io.IOException
import java.util.*
import kotlin.math.log


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
 * @date 2022-01-04 11:29
 * @describe TODO
 * @email 625805189@qq.com
 * @remark
 */
class LrcRepository {

    companion object {

        val TAG = "LrcRepository"

        val INSTANCE: LrcRepository by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            LrcRepository()
        }
    }

    suspend fun search(name: String): MutableList<Lrc> {
        val html = RxHttp.postBody(LrcUrl.SEARCH)
            .setDomainToLrcIfAbsent()
            .setBody(
                RequestBody.create(
                    "application/x-www-form-urlencoded".toMediaTypeOrNull(),
                    "keyboard=$name&show=title,newstext&tempid=1&tbname=lrc&mid=1&dopost=search"
                )
            )
            .toClass<String>()
            .await()

        return withContext(Dispatchers.IO) {
            val doc = Jsoup.parse(html)
            val elements = doc.select("div.toplist").select("li")
            val list = mutableListOf<Lrc>()
            for (element in elements) {
                val music = element.select("div.gequ").select("a").html()
                val singer = element.select("span.geshou").select("a").html()
                val downloadUrl = element.select("span.lrc").select("a").attr("href")
                val date = element.select("span.time").html()
                Log.e(TAG, "$music  $singer  $downloadUrl  $date")
                list.add(Lrc(music, singer, downloadUrl, date))
            }
            list
        }
    }


    fun download(url: String, filePath: String): Await<String> {
        return RxHttp.get(url)
            .setDomainToLrcIfAbsent()
            .toDownload(filePath)

    }

}