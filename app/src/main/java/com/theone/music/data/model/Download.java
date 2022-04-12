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
@Entity(tableName = "download",indices = {@Index(value = "localPath",unique = true)})
public class Download {

    public Download() {
    }

    @Ignore
    public Download(int musicId, String localPath, int status, long time) {
        this.musicId = musicId;
        this.localPath = localPath;
        this.status = status;
        this.time = time;
    }

    @PrimaryKey(autoGenerate = true)
    public int id;
    public int musicId;
    public String localPath = "";
    public int status = DownloadStatus.DOWNLOADING;
    public long time;


}
