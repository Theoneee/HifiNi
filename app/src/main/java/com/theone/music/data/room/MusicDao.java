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

import com.theone.music.data.model.Music;
import com.theone.music.data.model.User;

import java.util.List;

/**
 * @author The one
 * @date 2022-04-12 10:16
 * @describe User_Dao
 * @email 625805189@qq.com
 * @remark
 */
@Dao
public interface MusicDao {

    /**
     * 插入一条数据
     * @param music
     */
    @Insert
    void insert(@NonNull Music music);

    /**
     * 更新数据
     * @param music
     * @return
     */
    @Update
    int update(@NonNull Music music);


    /**
     * 删除一条数据
     * @param music
     * @return
     */
    @Delete
    int delete(@NonNull Music music);


    /**
     * 根据[shareUrl]删除一条数据
     * @param shareUrl 分享的Url
     * @return 删除状态
     */
    @Query("DELETE FROM MusicInfo WHERE shareUrl ==:shareUrl")
    int delete(@NonNull String shareUrl);

    /**
     * 根据用户id查询
     * @param id 用户id
     * @return  List<Music>
     */
    @Query("select * from MusicInfo where id =:id")
    List<Music> getMusicById(int id);

    /**
     * 根据分享的Url查询
     * @param shareUrl 分享的Url
     * @return  List<Music>
     */
    @Query("select * from MusicInfo where shareUrl ==:shareUrl")
    List<Music> getMusicsByShareUrl(String shareUrl);


    /**
     * 更新本地音乐的播放地址
     * @param shareUrl 分享的地址
     * @param url 播放地址
     * @param realUrl 实际播放地址
     * @return 更新结果
     */
    @Query("update MusicInfo set url = :url, realUrl =:realUrl  where shareUrl ==:shareUrl")
    int updateMusicUrl(String shareUrl,String url,String realUrl);

    /**
     * 获取登录用户收藏的音乐列表
     * @param userId 用户id
     * @param page  当前页数
     * @param pageSize 每页显示数量
     * @return List<Music>
     */
    @Query("select * from MusicInfo where userId ==:userId and collection = 1 order by createDate desc limit :pageSize offset (:page -1)*:pageSize")
    List<Music> getCollectionMusicList(int userId,int page,int pageSize);

    /**
     * 用于查询用户是否收藏当前音乐
     * @param userId 用户id
     * @param shareUrl 分享的Url
     * @return List<Music>
     */
    @Query("select * from MusicInfo where userId ==:userId and shareUrl ==:shareUrl and collection = 1")
    List<Music> getUserCollectionMusic(int userId,String shareUrl);


    /**
     * 更新用户收藏音乐
     * @param userId 用户id
     * @param shareUrl 分享的Url
     * @param collection 收藏状态
     * @param createDate 时间
     * @return int 更新结果
     */
    @Query("update MusicInfo set userId = :userId,collection = :collection, createDate =:createDate  where shareUrl ==:shareUrl")
    int updateCollectionMusic(int userId,String shareUrl,int collection,long createDate);

    /**
     * 获取播放历史音乐列表
     * @param page  当前页数
     * @param pageSize 每页显示数量
     * @return List<Music>
     */
    @Query("select * from MusicInfo order by lastPlayDate desc limit :pageSize offset (:page -1)*:pageSize")
    List<Music> getHistoryMusicList(int page,int pageSize);

    /**
     * 更新音乐的最后播放日期 - 播放记录
     * @param date 日期
     * @param shareUrl 分享地址
     * @return 更新状态
     */
    @Query("update MusicInfo set lastPlayDate = :date where shareUrl ==:shareUrl")
    int updateMusicLastPlayDate(long date,String shareUrl);

}
