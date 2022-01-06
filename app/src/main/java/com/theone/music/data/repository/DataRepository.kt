package com.theone.music.data.repository

import com.theone.common.ext.logI
import com.theone.lover.data.room.AppDataBase
import com.theone.music.data.model.Music
import com.theone.music.data.model.TestAlbum
import com.theone.music.data.room.MusicDao
import com.theone.music.net.NetConstant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import rxhttp.toStr
import rxhttp.wrapper.cahce.CacheMode
import rxhttp.wrapper.param.RxHttp
import rxhttp.wrapper.utils.GsonUtil
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

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
class DataRepository {

    companion object {
        val INSTANCE: DataRepository by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            DataRepository()
        }

        val MUSIC_DAO: MusicDao by lazy {
            AppDataBase.INSTANCE.musicDao()
        }
    }

    suspend fun request(url: String, vararg formatArgs: Any): String {
        return RxHttp.get(url, *formatArgs)
            .setCacheMode(CacheMode.READ_CACHE_FAILED_REQUEST_NETWORK)
            .setCacheValidTime(-1)
            .toStr()
            .await()
    }

    /**
     * 解析搜索
     * @param response String
     * @return List<Music>
     */
    private suspend fun parseMusicList(response: String): List<Music> {
        return withContext(Dispatchers.IO) {
            val list = mutableListOf<Music>()
            Jsoup.parse(response).run {
                val elements = select("li.media.thread.tap")
                val pageInfo = select("ul.pagination")

                for (element in elements) {
                    val author = element.select("span.haya-post-info-username").first().toString()
                    author.logI()
                    if (author.contains("Admin")) {
                        continue
                    }
                    with(element) {
                        val avatar = select("img.avatar-3").attr("src")
                        val body = select("div.subject.break-all").select("a").first()
                        val link = body.attr("href")
                        val info = body.html()
                        var author = ""
                        var name = info

                        with(info) {
                            if (contains("《") && contains("》")) {
                                val index = indexOf("《")
                                author = substring(0, index)
                                name = substring(index + 1, indexOf("》"))
                            }
                            if (author.isEmpty())
                                if (contains("「") && contains("」")) {
                                    val index = indexOf("「")
                                    author = substring(0, index)
                                    name = substring(index + 1, indexOf("」"))
                                }
                        }

                        list.add(
                            Music(
                                author = author,
                                pic = NetConstant.BASE_URL + avatar,
                                title = name,
                                shareUrl = link
                            )
                        )
                    }
                }
            }
            list
        }
    }

    /**
     * 解析音乐信息
     * @param response String
     * @return MusicInfo
     */
    private suspend fun parseMusicInfo(response: String): Music {
        return withContext(Dispatchers.IO) {
            var result = ""
            val elements = Jsoup.parse(response).select("script")
            for (element in elements) {
                val item = element.toString()
                if (item.contains("APlayer")) {
                    result = item
                    break
                }
            }
            val start = result.indexOf("[") + 1
            val end = result.indexOf("}") + 1
            result = result.substring(start, end)
            GsonUtil.fromJson<Music>(result, Music::class.java)
        }
    }

    /**
     * 获取重定向后的地址
     * @param url String
     * @return String
     */
    private suspend fun getRedirectUrl(url: String): String {
        return withContext(Dispatchers.IO) {
            val conn = URL(NetConstant.BASE_URL + url).openConnection() as HttpURLConnection
            conn.responseCode
            val realUrl = conn.url.toString()
            conn.disconnect()
            realUrl
        }
    }

    suspend fun get(url: String, vararg formatArgs: Any): List<Music> {
        return parseMusicList(request(url, *formatArgs))
    }

    suspend fun getMusicInfo(link: String): Music {
        val response = request(link)
        return parseMusicInfo(response).apply {
            shareUrl = link
            if (!url.startsWith("http")) {
                // 然后得到重定向后的地址
                realUrl = getRedirectUrl(url)
            }
        }
    }

    fun createAlbum(data: Music): TestAlbum {
        val artists = TestAlbum.TestArtist().apply {
            name = "UnKnown"
        }
        val music = mutableListOf<TestAlbum.TestMusic>().apply {
            add(TestAlbum.TestMusic().apply {
                musicId = UUID.randomUUID().toString()
                shareUrl = data.shareUrl
                coverImg = data.pic
                title = data.title
                author = data.author
                url = data.getMusicUrl()
                artist = artists
            })
        }

        return TestAlbum().apply {
            albumId = UUID.randomUUID().toString()
            title = "HiFiNi"
            summary = data.author
            artist = artists
            coverImg = data.pic
            musics = music
        }
    }

}