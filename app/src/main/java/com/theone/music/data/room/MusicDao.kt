package com.theone.music.data.room

import androidx.room.*
import com.theone.music.data.model.MusicInfo

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
 * @date 2021-10-08 09:31
 * @describe TODO
 * @email 625805189@qq.com
 * @remark
 */
@Dao
interface MusicDao {

    @Insert
    fun insert(backgrounds: List<MusicInfo>)

    @Update
    fun update(backgrounds: List<MusicInfo>):Int

    @Delete
    fun delete(backgrounds: List<MusicInfo>):Int

    @Query("DELETE FROM MusicInfo WHERE url ==:url")
    fun delete(url:String)

    @Query("select * from MusicInfo order by createDate desc ")
    suspend fun getMusicList():List<MusicInfo>

    @Query("select * from MusicInfo where shareUrl ==:url")
    suspend fun findMusics(url: String):List<MusicInfo>

}