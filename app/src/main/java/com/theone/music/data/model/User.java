package com.theone.music.data.model;//  ┏┓　　　┏┓
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

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.theone.music.data.constant.DownloadStatus;

/**
 * @author The one
 * @date 2022-04-12 11:29
 * @describe 用户数据
 * @email 625805189@qq.com
 * @remark
 */
@Entity(tableName = "user",indices = {@Index(value = "account",unique = true)})
public class User {

    public User() {
    }

    @Ignore
    public User(String account, String password) {
        this.account = account;
        this.password = password;
    }

    /**
     * id、主键 自增
     */
    @PrimaryKey(autoGenerate = true)
    public int id = 0;
    /**
     * 用户名
     */
    public String account = "";
    /**
     * 用户昵称
     */
    public String nickname = "";
    /**
     * 密码
     */
    public String password = "";
    /**
     * 性别
     */
    public int sex = 0;

}
