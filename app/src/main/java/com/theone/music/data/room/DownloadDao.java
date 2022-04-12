package com.theone.music.data.room;//  ┏┓　　　┏┓
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

import androidx.annotation.NonNull;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.theone.music.data.model.Download;
import com.theone.music.data.model.User;

import java.util.List;

/**
 * @author The one
 * @date 2022-04-12 10:16
 * @describe Download_Dao
 * @email 625805189@qq.com
 * @remark
 */
@Dao
public interface DownloadDao {

    /**
     * 插入一条数据
     * @param download
     */
    @Insert
    void insert(@NonNull Download download);

    /**
     * 更新数据
     * @param download
     * @return
     */
    @Update
    int update(@NonNull Download download);


    /**
     * 删除一条数据
     * @param download
     * @return
     */
    @Delete
    int delete(@NonNull Download download);


    /**
     * 查询下载记录
     * @param page
     * @param pageSize
     * @return
     */
    @Query("select * from download order by time desc limit :pageSize offset (:page -1)*:pageSize")
    List<Download> getDownloadList(int page,int pageSize);

    /**
     * 更新下载状态
     * @param status 下载状态 [DownloadStatus]
     * @param musicId 下载的音乐本地保存的id
     * @return 更新结果
     */
    @Query("update download set status =:status where musicId = :musicId ")
    int updateDownloadStatus(int status,int musicId);

}
