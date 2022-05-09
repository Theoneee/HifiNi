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

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.theone.music.app.ext.AppExtKt;

import java.util.Objects;

/**
 * @author The one
 * @date 2022-04-12 11:14
 * @describe 音乐数据
 * @email 625805189@qq.com
 * @remark
 */

/**
 * Room的使用  每个需要保存的表使用[@Entity]注解
 * indices-唯一键
  */
@Entity(tableName = "MusicInfo",indices = {@Index(value = "shareUrl",unique = true)})
public class Music implements Parcelable {

    /**
     * id、主键 自增
     */
    @PrimaryKey(autoGenerate = true)
    public int id;
    /**
     * 用户id 关联登录用户
     */
    public int userId;

    /**
     * 歌名
     */
    public String title = "";
    /**
     * 歌手
     */
    public String author = "";
    /**
     * 播放地址
     */
    public String url = "";
    /**
     * 封面图片地址
     */
    public String pic = "";
    /**
     * 分享界面的Url
     */
    public String shareUrl;
    /**
     * 实际的播放地址 - 可能会通过播放地址【域名重定向】得到一个实际的播放地址
     */
    public String realUrl = "";
    /**
     * 创建时间 - 用于排序
     */
    public long createDate = 0;
    /**
     * 最后播放时间 - 用于最近播放排序
     */
    public long lastPlayDate = 0;
    /**
     * 是否收藏 0 未收藏 1 已收藏
     */
    public int collection;



    @Ignore
    public Music(TestAlbum.TestMusic music) {
        this.title = music.getTitle();
        this.author = music.getAuthor();
        this.url = music.getUrl();
        this.pic = music.getCoverImg();
        this.shareUrl = music.getShareUrl();
    }

    public String getMusicUrl(){
        if(realUrl.isEmpty()){
            return url;
        }
        return realUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Music music = (Music) o;
        return Objects.equals(shareUrl, music.shareUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(shareUrl);
    }

    public CharSequence getAuthorHtml(){
        return AppExtKt.getHtmlString(author);
    }

    public CharSequence getTitleHtml(){
        return AppExtKt.getHtmlString(title);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.userId);
        dest.writeString(this.title);
        dest.writeString(this.author);
        dest.writeString(this.url);
        dest.writeString(this.pic);
        dest.writeString(this.shareUrl);
        dest.writeString(this.realUrl);
        dest.writeLong(this.createDate);
        dest.writeLong(this.lastPlayDate);
        dest.writeInt(this.collection);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readInt();
        this.userId = source.readInt();
        this.title = source.readString();
        this.author = source.readString();
        this.url = source.readString();
        this.pic = source.readString();
        this.shareUrl = source.readString();
        this.realUrl = source.readString();
        this.createDate = source.readLong();
        this.lastPlayDate = source.readLong();
        this.collection = source.readInt();
    }

    public Music() {
    }

    protected Music(Parcel in) {
        this.id = in.readInt();
        this.userId = in.readInt();
        this.title = in.readString();
        this.author = in.readString();
        this.url = in.readString();
        this.pic = in.readString();
        this.shareUrl = in.readString();
        this.realUrl = in.readString();
        this.createDate = in.readLong();
        this.lastPlayDate = in.readLong();
        this.collection = in.readInt();
    }

    public static final Parcelable.Creator<Music> CREATOR = new Parcelable.Creator<Music>() {
        @Override
        public Music createFromParcel(Parcel source) {
            return new Music(source);
        }

        @Override
        public Music[] newArray(int size) {
            return new Music[size];
        }
    };
}
