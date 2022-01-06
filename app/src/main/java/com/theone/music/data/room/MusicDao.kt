package com.theone.music.data.room

import androidx.room.*
import com.theone.music.data.model.Music

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
    fun insert(backgrounds: List<Music>)

    @Update
    fun update(backgrounds: List<Music>):Int

    @Delete
    fun delete(backgrounds: List<Music>):Int

    @Query("DELETE FROM MusicInfo WHERE shareUrl ==:shareUrl")
    fun delete(shareUrl:String)

    @Query("select * from MusicInfo order by createDate desc ")
    suspend fun getMusicList():List<Music>

    @Query("select * from MusicInfo where shareUrl ==:shareUrl")
    suspend fun findMusics(shareUrl: String):List<Music>

}