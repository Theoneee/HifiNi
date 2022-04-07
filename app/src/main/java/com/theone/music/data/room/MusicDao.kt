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
    fun insert(music: Music)

    @Insert
    fun insert(musics: List<Music>)

    @Update
    fun update(music: Music): Int

    @Update
    fun update(musics: List<Music>): Int

    @Delete
    fun delete(musics: List<Music>): Int

    @Query("DELETE FROM MusicInfo WHERE shareUrl ==:shareUrl")
    fun delete(shareUrl: String)

    @Query("select * from MusicInfo where userId ==:userId and collection = 1 order by createDate desc ")
    fun getCollectionMusicList(userId: Int): List<Music>

    @Query("select * from MusicInfo where shareUrl ==:shareUrl")
    fun findMusics(shareUrl: String): List<Music>

    @Query("select * from MusicInfo where userId ==:userId and shareUrl ==:shareUrl and collection = 1")
    fun findCollectionMusics(userId: Int,shareUrl: String): List<Music>

    @Query("update MusicInfo set userId = :userId,collection = :collection, createDate =:createDate  where shareUrl ==:shareUrl ")
    fun updateCollectionMusic(
        userId: Int,
        shareUrl: String,
        collection: Int,
        createDate: Long
    )

    @Query("update MusicInfo set url = :url, realUrl =:realUrl  where shareUrl ==:shareUrl")
    fun updateDataBaseMusic(
        shareUrl: String,
        url: String,
        realUrl: String
    )

}