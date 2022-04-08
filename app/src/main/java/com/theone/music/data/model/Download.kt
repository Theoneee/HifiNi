package com.theone.music.data.model

import androidx.room.*

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
 * @date 2022-04-08 16:58
 * @describe TODO
 * @email 625805189@qq.com
 * @remark
 */

@Entity(tableName = "download", indices = [Index(value = ["localPath"], unique = true)])
data class Download(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val musicId: Int = 0,
    val localPath: String = "",
    val time: Long = 0
) {

    @Ignore
    constructor() : this(0) {}

}