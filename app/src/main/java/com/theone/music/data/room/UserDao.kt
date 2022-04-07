package com.theone.music.data.room

import androidx.room.*
import com.theone.music.data.model.Music
import com.theone.music.data.model.User

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
interface UserDao {

    @Insert
    fun insert(user: User)

    @Update
    fun update(user: User): Int

    @Delete
    fun delete(user: User): Int

    @Query("select * from user where account == :account")
    fun getUserList(account:String): List<User>

}