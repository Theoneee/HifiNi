package com.theone.music.data.room;
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

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.theone.music.data.model.Download;
import com.theone.music.data.model.Music;
import com.theone.music.data.model.User;
import com.theone.mvvm.base.BaseApplicationKt;

/**
 * @author The one
 * @date 2022-04-12 10:44
 * @describe TODO
 * @email 625805189@qq.com
 * @remark
 */
@Database(entities = {Music.class, User.class, Download.class}, version = 1, exportSchema = false)
public abstract class AppDataBase extends RoomDatabase {

    public abstract MusicDao musicDao();

    public abstract UserDao userDao();

    public abstract DownloadDao downloadDao();

    private static volatile AppDataBase sInstance;

    private static final String DB_NAME = "HifiNi_Java.db";

    /**
     * 懒汉单例模式
     * @return
     */
    public static AppDataBase getInstance() {
        if (null == sInstance) {
            synchronized (AppDataBase.class) {
                if (null == sInstance) {
                    sInstance = Room.databaseBuilder(
                            BaseApplicationKt.getAppContext(),
                            AppDataBase.class,
                            DB_NAME
                    ).allowMainThreadQueries().build();
                }
            }
        }
        return sInstance;
    }


}
