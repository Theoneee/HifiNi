package com.theone.music.data.repository

import android.util.Log
import com.theone.common.ext.getNumbers
import com.theone.lover.data.room.AppDataBase
import com.theone.music.data.model.*
import com.theone.music.data.room.DownloadDao
import com.theone.music.data.room.MusicDao
import com.theone.music.data.room.UserDao
import com.theone.music.net.NetConstant
import com.theone.music.player.PlayerManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import rxhttp.toStr
import rxhttp.wrapper.cahce.CacheMode
import rxhttp.wrapper.param.RxHttp
import rxhttp.wrapper.utils.GsonUtil
import java.lang.StringBuilder
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

        val USER_DAO: UserDao by lazy {
            AppDataBase.INSTANCE.userDao()
        }

        val DOWNLOAD_DAO: DownloadDao by lazy {
            AppDataBase.INSTANCE.downloadDao()
        }

    }

    /**
     * https://hifini.com/
     * url = forum-15.htm
     */
    suspend fun request(url: String, vararg formatArgs: Any): String {
        return RxHttp.get(url, *formatArgs)
            // 不再设置缓存了，第一次请求成功后，本地数据库已经保存了播放信息，这里必要要从网络获取最新的
            // 设置缓存模式： 先从缓存获取，如果缓存没有再请求网络
            //.setCacheMode(CacheMode.READ_CACHE_FAILED_REQUEST_NETWORK)
            // 这是缓存时间
            //.setCacheValidTime(-1)
            .setCacheMode(CacheMode.ONLY_NETWORK)
            .toStr()
            .await()
    }

    suspend fun getSingerList(url: String): List<Singer> {
        val response = request(url)
        return withContext(Dispatchers.IO) {
            val list = mutableListOf<Singer>()
            Jsoup.parse(response).run {
                val elements = getElementById("taghot").select("a")
                for (element in elements) {
                    val name = element.html()
                    val url = element.attr("href")
                    list.add(Singer(name, url))
                }
            }
            list
        }
    }

    private fun Document.parseTotalPage(): Int {
        return try {
            val elements = select("li.page-item").select("a")
            val pageInfo = elements[elements.size - 2]
            var page = pageInfo.html()
            if (page.contains("...")) {
                page = page.replace("...", "")
            }
            page.toInt()
        } catch (e: Exception) {
            e.printStackTrace()
            1
        }
    }


    /**
     * 解析歌手搜索
     * @param response String
     * @return List<Music>
     */
    private suspend fun parsesSingerMusicList(response: String): ResponseList<Music> {
        return withContext(Dispatchers.IO) {
            Jsoup.parse(response).run {
                val list = mutableListOf<Music>()
                val elements = select("li.media.thread.tap")
                val totalPage = parseTotalPage()
                for (element in elements) {
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
                ResponseList(list, totalPage)
            }
        }
    }


    /**
     * 解析搜索
     * @param response String
     * @return List<Music>
     */
    private suspend fun parseMusicList(response: String): ResponseList<Music> {
        return withContext(Dispatchers.IO) {
            Jsoup.parse(response).run {
                val list = mutableListOf<Music>()
                val elements = select("li.media.thread.tap")
                val totalPage = parseTotalPage()

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
                ResponseList(list, totalPage)
            }
        }
    }

    /**
     * 解析音乐播放信息
     * @param response String
     * @return MusicInfo
     *
     * 用Jsoup工具解析html信息
     */
    private suspend fun parseMusicInfo(response: String): Music {
        return withContext(Dispatchers.IO) {
            var result = ""
            val doc = Jsoup.parse(response)
            doc.parsePassword()
            val elements = doc.select("script")
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
     * 解析提取码
     * @receiver Document
     */
    private fun Document.parsePassword() {
        val auths = select("div.container").select("style")
        val validKeys = mutableListOf<String>()
        for (index in 0..1) {
            try {
                val validItems = auths[index].toString().replace(".", "").replace("<style>","").run {
                    substring(0, indexOf("{"))
                }.split(",")
                validKeys.addAll(validItems)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        val pans = select("div.message.break-all").select("p")
        var urls = mutableListOf<String>()
        for (pan in pans){
            val href = pan.select("a").attr("href")
            if(!href.isNullOrEmpty()&&href.startsWith("http")){
                urls.add(href)
                break
            }
        }
        val list = select("div.alert.alert-success")
        val passwordItems = mutableListOf<String>()
        val sb = StringBuilder()
        for (link in list) {
            sb.setLength(0)
            val href = link.select("a").attr("href")
            if(!href.isNullOrEmpty()&&href.startsWith("http")){
                urls.add(href)
            }
            val spans = link.select("span")
            for (span in spans) {
                val value = span.html()
                val key = span.attr("class")
                if (key in validKeys) {
                    sb.append(value)
                }
            }
            passwordItems.add(sb.toString())
        }
        Log.e(TAG, "urls: $urls")
        Log.e(TAG, "passwordItems: $passwordItems")
    }

    /**
     * 获取重定向后的地址
     * @param url String
     * @return String
     */
    private suspend fun getRedirectUrl(url: String): String {
        return withContext(Dispatchers.IO) {
            val conn = URL(url).openConnection() as HttpURLConnection
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
        val conn = URL(url).openConnection() as HttpURLConnection
        conn.setRequestProperty("referer", NetConstant.BASE_URL)
        val code = conn.responseCode
        conn.disconnect()
        // 不为200的或者跳到了腾讯网了都算作废
        return code != 200 || conn.url.toString().contains("https://www.qq.com/")
    }

    suspend fun get(url: String, vararg formatArgs: Any): ResponseList<Music> {
        return parseMusicList(request(url, *formatArgs))
    }

    suspend fun getSingerList(url: String, vararg formatArgs: Any): ResponseList<Music> {
        return parsesSingerMusicList(request(url, *formatArgs))
    }

    fun getDbMusicInfo(link: String): Music? {
        val list = MUSIC_DAO.findMusics(link)
        return if (list.isNotEmpty() && list[0].getMusicUrl().isNotEmpty()) {
            list[0]
        } else {
            null
        }
    }

    suspend fun getMusicInfo(link: String, isReload: Boolean): Music {
        if (!isReload) {
            // 先从数据里查是否有
            val list = MUSIC_DAO.findMusics(link)
            if (list.isNotEmpty() && list[0].getMusicUrl().isNotEmpty()) {
                val music = list[0]
                if (music.getMusicUrl().isNotEmpty()) {
                    if (checkUrl(music.getMusicUrl())) {
                        return music
                    }
                }
            }
        }
        // 请求 https://hifini.com/thread-35837.htm
        // response 其实一个html 信息
        // 我们最后想要拿到的是一个ListBean
        val response = request(link)
        // 解析
        val music = parseMusicInfo(response).apply {
            shareUrl = link
            if (!url.startsWith("http")) {
                url = NetConstant.BASE_URL + url
                // 然后得到重定向后的地址
                realUrl = getRedirectUrl(url)
                // 其实这里可以不需要这个实际播放地址（重定向过后的）
                // 但是这里得到的信息返回成功后界面将会显示内容层
                // 后续再交给PlayerManager去重定向缓冲等耗时操作，界面的等待时间就会增长
                // 那直接把这个等待时间给到加载界面吧
            }
        }
        // 重新加载的播放地址，更新数据库
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

    /**
     * 快手、抖音 搜 MV
     *
     * 分享- 复制连接
     *
     * 微信小程序 - 快速去水印
     *
     * 视频封面、播放地址
     */
    fun getMvList(): List<MV> {
        return listOf(
            MV(
                "http://tx2.a.kwimgs.com/upic/2022/01/17/00/BMjAyMjAxMTcwMDE4NDdfMTQzNjU5OTY5MV82NTA0MDU1MDUxMF8xXzM=_B22e69f52692cbbd31c865b1ee3c74d3e.jpg?tag=1-1649396559-std-0-fcr6ceoh1h-7a923886ee61a21f&clientCacheKey=3xs7pazsdsvk93g.jpg&di=79c43beb&bp=12681",
                "https://txmov2.a.kwimgs.com/upic/2022/01/17/00/BMjAyMjAxMTcwMDE4NDdfMTQzNjU5OTY5MV82NTA0MDU1MDUxMF8xXzM=_b_B43b19cec0f504b6d89438917f1dc168d.mp4?tag=1-1649396559-std-1-tifdlurm9q-4bd7f2845742773f&clientCacheKey=3xs7pazsdsvk93g_b.mp4&tt=b&di=79c43beb&bp=12681",
                "后来-万人合唱版", "刘若英"
            ),
            MV(
                "http://ali2.a.kwimgs.com/upic/2021/07/09/20/BMjAyMTA3MDkyMDM4NDBfMTUxNDcxODUyNl81MjkyMjYyNTkwOV8yXzM=_ev2_low_B30c714730abb1dbebf770def676372b4.webp?tag=1-1649396252-std-0-cwgapyqbpb-052a59a6e9d863af&clientCacheKey=3x4n4nurfnpn7wc_ev2_low.webp&di=529d11df&bp=12681",
                "https://txmov2.a.kwimgs.com/upic/2021/07/09/20/BMjAyMTA3MDkyMDM4NDBfMTUxNDcxODUyNl81MjkyMjYyNTkwOV8yXzM=_b_Beba860dec87a4dde7b83cfc4748a94df.mp4?tag=1-1649396252-std-1-eu7e6yiip9-6368008128fe8011&clientCacheKey=3x4n4nurfnpn7wc_b.mp4&tt=b&di=529d11df&bp=12681",
                "万疆", "李玉刚"
            ),
            MV(
                "https://txmov2.a.kwimgs.com/upic/2021/12/21/10/BMjAyMTEyMjExMDE3MjlfMTY3NzUyNDAzXzYzMjEwODg4NjcwXzJfMw==_b_B738e41a16c925a9901596995228d9880.mp4",
                "https://txmov2.a.kwimgs.com/upic/2021/12/21/10/BMjAyMTEyMjExMDE3MjlfMTY3NzUyNDAzXzYzMjEwODg4NjcwXzJfMw==_b_B738e41a16c925a9901596995228d9880.mp4",
                "相思", "毛阿敏"
            ),
            MV(
                "http://tx2.a.kwimgs.com/upic/2022/01/17/00/BMjAyMjAxMTcwMDE4NDdfMTQzNjU5OTY5MV82NTA0MDU1MDUxMF8xXzM=_B22e69f52692cbbd31c865b1ee3c74d3e.jpg?tag=1-1649396559-std-0-fcr6ceoh1h-7a923886ee61a21f&clientCacheKey=3xs7pazsdsvk93g.jpg&di=79c43beb&bp=12681",
                "https://txmov2.a.kwimgs.com/upic/2022/01/17/00/BMjAyMjAxMTcwMDE4NDdfMTQzNjU5OTY5MV82NTA0MDU1MDUxMF8xXzM=_b_B43b19cec0f504b6d89438917f1dc168d.mp4?tag=1-1649396559-std-1-tifdlurm9q-4bd7f2845742773f&clientCacheKey=3xs7pazsdsvk93g_b.mp4&tt=b&di=79c43beb&bp=12681",
                "后来-万人合唱版", "刘若英"
            )
        )
    }

    fun getBannerList() = arrayListOf(
        Banner("https://y.qq.com/music/common/upload/MUSIC_FOCUS/4279661.jpg?max_age=2592000"),
        Banner("https://y.qq.com/music/common/upload/MUSIC_FOCUS/4280047.jpg?max_age=2592000"),
        Banner("https://y.qq.com/music/common/upload/MUSIC_FOCUS/4279664.jpg?max_age=2592000")
    )

}