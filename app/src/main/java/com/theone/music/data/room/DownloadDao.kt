package com.theone.music.data.room

import androidx.room.*
import com.theone.music.data.model.Download

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
interface DownloadDao {

    @Insert
    fun insert(bean: Download)

    @Update
    fun update(bean: Download): Int

    @Delete
    fun delete(bean: Download): Int

    @Query("select * from download order by time desc limit :pageSize offset (:page -1)*:pageSize")
    fun getDownloadList(page:Int,pageSize:Int): List<Download>

    @Query("update download set status =:status where musicId = :musicId ")
    fun updateDownloadStatus(status:Int,musicId:Int):Int

}