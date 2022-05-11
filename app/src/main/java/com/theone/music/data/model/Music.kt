package com.theone.music.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey
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
    var userId: Int = 0,
    var title: String = "",
    var author: String = "",
    var url: String = "",
    var pic: String = "",
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
        if(url.isNotEmpty()&&!url.startsWith("http")){
            NetConstant.BASE_URL+url
        }else{
            url
        }
    }

    fun getAuthorHtml(): CharSequence {
        return author.getHtmlString()
    }

    fun getTitleHtml(): CharSequence {
        return title.getHtmlString()
    }

    override fun toString(): String {
        return "Music(id=$id, title='$title', author='$author', url='$url', pic='$pic', shareUrl='$shareUrl', realUrl='$realUrl', createDate=$createDate, collection=$collection)"
    }

}