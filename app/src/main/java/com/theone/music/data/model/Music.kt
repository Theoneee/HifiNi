package com.theone.music.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.theone.music.app.ext.getHtmlString
import com.theone.music.net.NetConstant
import kotlinx.android.parcel.Parcelize

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

@Entity(tableName = "MusicInfo", indices = [Index(value = ["shareUrl"], unique = true)])
@Parcelize
data class Music(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var username: String = "",
    @SerializedName("title")
    var name: String = "",
    @SerializedName("author")
    var singer: String = "",
    @SerializedName("pic")
    var cover: String = "",
    @SerializedName("url")
    var playUrl: String = "",
    var shareUrl: String = "",
    var realUrl: String = "",
    var createDate: Long = 0,
    var lastPlayDate: Long = 0,
    var collection: Int = 0
) : Parcelable {

    @Ignore
    constructor() : this(0) {
    }

    fun getMusicUrl(): String = realUrl.ifEmpty {
        if (playUrl.isNotEmpty() && !playUrl.startsWith("http")) {
            NetConstant.BASE_URL + playUrl
        } else {
            playUrl
        }
    }

    fun getCoverUrl():String {
        if (!cover.startsWith("http")) {
            cover = "https://www.hifini.com/upload/forum/1.png"
        }
        return cover
    }

    fun getAuthorHtml(): CharSequence {
        return singer.getHtmlString()
    }

    fun getTitleHtml(): CharSequence {
        return name.getHtmlString()
    }

    override fun toString(): String {
        return "Music(id=$id, title='$name', author='$singer', url='$playUrl', pic='$cover', shareUrl='$shareUrl', realUrl='$realUrl', createDate=$createDate, collection=$collection)"
    }

}