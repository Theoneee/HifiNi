package com.theone.music.data.repository

import android.util.Log
import com.theone.common.ext.logI
import com.theone.lover.data.room.AppDataBase
import com.theone.music.data.model.Music
import com.theone.music.data.model.TestAlbum
import com.theone.music.data.room.MusicDao
import com.theone.music.data.room.UserDao
import com.theone.music.net.NetConstant
import com.theone.music.player.PlayerManager
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

        val TAG = "DataRepository"

        val INSTANCE: DataRepository by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            DataRepository()
        }

        val MUSIC_DAO: MusicDao by lazy {
            AppDataBase.INSTANCE.musicDao()
        }

        val USER_DAO:UserDao by lazy {
            AppDataBase.INSTANCE.userDao()
        }

    }

    suspend fun request(url: String, vararg formatArgs: Any): String {
        return RxHttp.get(url, *formatArgs)
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

    /**
     * 检查播放地址是否有效/已过期
     * https://ws.stream.qqmusic.qq.com/C400000ka29V3B9e72.m4a?guid=68890724&vkey=6D57BD030F12173BB569CB9F28FAA32FD93AE71D8827D51FB08E4DC7FFB13ADAA29908780A276F19E58A539FC9EAB9F3B6ECB50B991282A1&uin=&fromtag=143
     * 比如这种连接，过期了，403异常，而本地又没有缓存时，需要重新获取播放地址
     * @param url String
     * @return String
     */
    fun checkUrl(url: String): Boolean {
        // 先判断本地有没有缓存，有缓存后续就会用
        val cacheUrl = PlayerManager.getInstance().getCacheUrl(url)
        if(cacheUrl.contains("storage/emulated")){
            Log.e(TAG, "checkUrl: $cacheUrl" )
            return false
        }
        val conn = URL(url).openConnection() as HttpURLConnection
        val code = conn.responseCode
        Log.e(TAG, "checkUrl: $code  ${conn.url}" )
        conn.disconnect()
        // 403的或者跳到了腾讯网了都算作废
        return code == 403 || conn.url.toString().contains("https://www.qq.com/")
    }

    suspend fun get(url: String, vararg formatArgs: Any): List<Music> {
        return parseMusicList(request(url, *formatArgs))
    }

    suspend fun getMusicInfo(link: String, isReload: Boolean): Music {
        if (!isReload) {
            // 先从数据里查是否有
            val list = MUSIC_DAO.findMusics(link)
            if (list.isNotEmpty()) {
                return list[0]
            }
        }
        val response = request(link)
        val music = parseMusicInfo(response).apply {
            shareUrl = link
            if (!url.startsWith("http")) {
                // 然后得到重定向后的地址
                realUrl = getRedirectUrl(url)
            }
        }
        Log.e(TAG, "getMusicInfo: $music" )
        if (isReload) {
            MUSIC_DAO.updateDataBaseMusic(link, music.url, music.realUrl)
        } else {
            music.createDate = System.currentTimeMillis()
            MUSIC_DAO.insert(music)
        }
        return music
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