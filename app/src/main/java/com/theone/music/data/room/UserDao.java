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
public interface UserDao {

    /**
     * 插入一条数据
     * @param user
     */
    @Insert
    void insert(@NonNull User user);

    /**
     * 更新数据
     * @param user
     * @return
     */
    @Update
    int update(@NonNull User user);


    /**
     * 删除一条数据
     * @param user
     * @return
     */
    @Delete
    int delete(@NonNull User user);


    /**
     * 根据用户名查询
     * @param account
     * @return
     */
    @Query("select * from user where account == :account")
    List<User> getUserList(@NonNull String account);

}
