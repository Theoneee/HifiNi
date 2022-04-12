package com.theone.music.service;//  ┏┓　　　┏┓
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

import android.app.Activity;
import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.hjq.toast.ToastUtils;
import com.theone.common.constant.BundleConstant;
import com.theone.music.R;
import com.theone.music.data.constant.DownloadStatus;
import com.theone.music.data.model.Download;
import com.theone.music.data.model.Music;
import com.theone.music.data.repository.DataRepository;
import com.theone.mvvm.core.app.util.FileDirectoryManager;
import com.theone.mvvm.core.app.util.NotificationManager;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;
import java.util.List;
import java.util.UUID;

import okhttp3.Call;

/**
 * @author The one
 * @date 2022-04-11 16:36
 * @describe 下载服务
 * @email 625805189@qq.com
 * @remark
 */
public class DownloadService extends Service {

    public static void start(Activity activity, Music music){
        Intent intent = new Intent(activity,DownloadService.class);
        intent.putExtra(BundleConstant.DATA,music);
        activity.startService(intent);
    }


    private Music mDownload;
    private NotificationCompat.Builder mNotificationBuilder;
    private int NOTIFICATION_ID = 0;
    private int mOldPercent = 0;
    private int musicId = 0;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (null != intent && null == mDownload) {
            NOTIFICATION_ID = UUID.randomUUID().hashCode();
            mDownload = intent.getParcelableExtra(BundleConstant.DATA);
            initNotification();
            startDown();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void startDown() {
        String url = mDownload.getMusicUrl();
        // 文件后缀
        String type = url.contains("mp3") ? "mp3" : "m4a";
        // 五月天-天使.mp3
        String name = mDownload.author + "-" + mDownload.title + "." + type;
        // 保存的地址  APP名字/Download/Music
        String downloadPath = FileDirectoryManager.INSTANCE.getDownloadPath() + File.separator + "Music";
        // 数据库里拿到MusicId
        List<Music> musics = DataRepository.Companion.getMUSIC_DAO().getMusicsByShareUrl(mDownload.shareUrl);
        musicId = musics.get(0).id;
        // 创建一个下载对象
        Download download = new Download(musicId, downloadPath + File.separator + name, DownloadStatus.DOWNLOADING, System.currentTimeMillis());
        // 添加到数据库
        DataRepository.Companion.getDOWNLOAD_DAO().insert(download);

        OkHttpUtils.get()
                .url(url)
                .tag(url)
                .build()
                .execute(new FileCallBack(downloadPath, name) {

                    @Override
                    public void inProgress(float progress, long total, int id) {
                        int percent = (int) (progress * 100);
                        if (percent != mOldPercent) {
                            mOldPercent = percent;
                            updateProgress(percent);
                        }
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        updateDownloadDb(DownloadStatus.FAIL);
                        updateNotification("下载失败", false);
                        ToastUtils.show("下载失败 " + e.getLocalizedMessage());
                        File file = new File(downloadPath, name);
                        if (file.exists()) {
                            file.delete();
                        }
                    }

                    @Override
                    public void onResponse(File response, int id) {
                        updateDownloadDb(DownloadStatus.SUCCESS);
                        updateLocalFile(response);
                        updateNotification("下载完成", true);
                    }

                });

    }


    /**
     * 更新通知栏状态
     *
     * @param title
     * @param isFinish
     */
    private void updateNotification(String title, boolean isFinish) {
        stopForeground(true);
        NotificationCompat.Builder builder = NotificationManager.Companion.getInstance().createNotification(NOTIFICATION_ID, title, title, mDownload.getMusicUrl(),
                NotificationManager.LEVEL_DEFAULT_CHANNEL_ID,
                true, isFinish ? R.drawable.service_down_finish : R.drawable.service_down);
        builder.setDefaults(Notification.DEFAULT_VIBRATE);
        builder.setAutoCancel(isFinish);
        Notification notification = builder.build();
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        NotificationManager.Companion.getInstance().notify(NOTIFICATION_ID, builder);
        stopSelf();
    }

    /**
     * 刷新本地文件
     *
     * @param response
     */
    private void updateLocalFile(File response) {
        try {
            MediaScannerConnection.scanFile(this, new String[]{response.getAbsolutePath()},null,new MediaScannerConnection.OnScanCompletedListener(){

                @Override
                public void onScanCompleted(String path, Uri uri) {
                    ToastUtils.show("已保存到: "+path);
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 更新数据库里的下载状态
     *
     * @param status
     */
    private void updateDownloadDb(int status) {
        DataRepository.Companion.getDOWNLOAD_DAO().updateDownloadStatus(status, musicId);
    }

    /**
     * 更新通知栏下载进度
     *
     * @param percent
     */
    private void updateProgress(int percent) {
        mNotificationBuilder.setContentTitle("下载中");
        mNotificationBuilder.setContentText(percent + "%");
        mNotificationBuilder.setProgress(100, percent, false);
        NotificationManager.Companion.getInstance().notify(NOTIFICATION_ID, mNotificationBuilder);
    }

    /**
     * 初始化通知栏
     */
    private void initNotification() {
        mNotificationBuilder = NotificationManager.Companion.getInstance().createNotification(
                NOTIFICATION_ID,
                "开始下载",
                "开始下载",
                mDownload.getMusicUrl(),
                NotificationManager.LEVEL_DEFAULT_CHANNEL_ID,
                true, R.drawable.service_down
        );
        mNotificationBuilder.setOngoing(true);
        startForeground(NOTIFICATION_ID, mNotificationBuilder.build());
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
