package com.theone.music.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.theone.common.callback.IImageUrl
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

@Entity(tableName = "MusicInfo",indices = [Index(value = ["shareUrl"],unique = true)])
@Parcelize
data class MusicInfo(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var title: String = "",
    var author: String = "",
    var url: String = "",
    var pic: String = "",
    var shareUrl: String = "",
    var realUrl: String = "",
    var createDate: Long = 0
) : Parcelable {

    constructor(music: TestAlbum.TestMusic?):this(){
        music?.run {
            this@MusicInfo.title = title
            this@MusicInfo.author = author
            this@MusicInfo.pic = coverImg
            this@MusicInfo.url = url
            this@MusicInfo.shareUrl = shareUrl
            this@MusicInfo.author = author
        }
    }

    fun getMusicUrl(): String = if (realUrl.isEmpty()) url else realUrl

    fun getAuthorHtml(): CharSequence{
        return author.getHtmlString()
    }

    fun getTitleHtml(): CharSequence{
        return title.getHtmlString()
    }

    override fun toString(): String {
        return "MusicInfo(id=$id, title='$title', author='$author', url='$url', pic='$pic', shareUrl='$shareUrl', realUrl='$realUrl', createDate=$createDate)"
    }


}