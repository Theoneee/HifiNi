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
 * @describe TODO
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

    @PrimaryKey(autoGenerate = true)
    public int id;
    public String account = "";
    public String nickname;
    public String password = "";
    public int sex;


}
