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

/**
 * @author The one
 * @date 2022-04-12 13:15
 * @describe TODO
 * @email 625805189@qq.com
 * @remark
 */
public class Singer implements Parcelable {

    private String name;
    private String url;

    public Singer() {
    }

    public Singer(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.url);
    }

    public void readFromParcel(Parcel source) {
        this.name = source.readString();
        this.url = source.readString();
    }

    protected Singer(Parcel in) {
        this.name = in.readString();
        this.url = in.readString();
    }

    public static final Parcelable.Creator<Singer> CREATOR = new Parcelable.Creator<Singer>() {
        @Override
        public Singer createFromParcel(Parcel source) {
            return new Singer(source);
        }

        @Override
        public Singer[] newArray(int size) {
            return new Singer[size];
        }
    };
}
