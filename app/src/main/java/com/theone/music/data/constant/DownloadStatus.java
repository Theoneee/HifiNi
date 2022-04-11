package com.theone.music.data.constant;


/**
 * @author
 * @des 下载状态常量
 */
public class DownloadStatus {

   /**
    * 下载中
    */
   public static final int DOWNLOADING = 0;
   /**
    * 下载成功
    */
   public static final int SUCCESS = 1;
   /**
    * 下载失败
    */
   public static final int FAIL = 2;
   /**
    * 下载成功但是文件被删除
    */
   public static final int FILE_DELETE = 3;

}
