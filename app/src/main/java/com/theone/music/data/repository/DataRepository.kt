package com.theone.music.data.repository

import android.util.Log
import com.theone.common.ext.logE
import com.theone.common.ext.logI
import com.theone.common.ext.matchResult
import com.theone.common.ext.trimAll
import com.theone.music.app.ext.writeStringToFile
import com.theone.music.data.model.Music
import com.theone.music.data.model.MusicInfo
import com.theone.music.net.NetConstant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Response
import org.jsoup.Jsoup
import rxhttp.toOkResponse
import rxhttp.toStr
import rxhttp.wrapper.coroutines.Await
import rxhttp.wrapper.param.RxHttp
import rxhttp.wrapper.utils.GsonUtil
import java.util.regex.Pattern

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
object DataRepository {

    suspend fun search(keyWord: String): List<Music> {
        val response = RxHttp.get(NetConstant.SEARCH, keyWord)
            .toStr()
            .await()
        return parseSearch(response)
    }

    private suspend fun parseSearch(response: String): List<Music> {
        return withContext(Dispatchers.IO) {
            val list = mutableListOf<Music>()
            Jsoup.parse(response).run {
                val elements = select("li.media.thread.tap")
                val pageInfo = select("ul.pagination")

                for (element in elements) {
                    with(element) {
                        val avatar = select("img.avatar-3").attr("src")
                        val body = select("div.subject.break-all").select("a").first()
                        val link = body.attr("href")
                        val name = body.html()
                        "avatar: $avatar  link: $link  name: $name".logI()
                        list.add(Music(NetConstant.BASE_URL + avatar, name, link))
                    }
                }
            }
            list
        }
    }

    suspend fun getMusicInfo(link: String): MusicInfo {
        val response = RxHttp.get(link)
            .toStr()
            .await()
        var result = ""
        try {
            val elements = Jsoup.parse(response).select("script")
            for (element in elements) {
                val item = element.toString()
                if (item.contains("APlayer")) {
                    result = item
                    break
                }
            }
            if(result.isEmpty()){
                TODO("暂无资源")
            }
            val start = result.indexOf("[")+1
            val end = result.indexOf("}")+1
            result = result.substring(start, end)
            val musicInfo = GsonUtil.fromJson<MusicInfo>(result,MusicInfo::class.java)
            return musicInfo
        }catch (e:Exception){
            TODO("暂无资源")
        }

    }

}