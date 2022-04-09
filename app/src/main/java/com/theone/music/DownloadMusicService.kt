package com.theone.music

import android.app.Activity
import android.app.Notification
import android.app.Service
import android.content.Intent
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.hjq.toast.ToastUtils
import com.theone.common.constant.BundleConstant
import com.theone.music.data.constant.DownloadStatus
import com.theone.music.data.model.Download
import com.theone.music.data.model.Music
import com.theone.music.data.repository.DataRepository
import com.theone.mvvm.base.appContext
import com.theone.mvvm.core.app.util.FileDirectoryManager
import com.theone.mvvm.core.app.util.NotificationManager
import com.zhy.http.okhttp.OkHttpUtils
import com.zhy.http.okhttp.callback.FileCallBack
import okhttp3.Call
import java.io.File
import java.util.*

fun Activity.startMusicDownloadService(download: Music) {
    val intent = Intent(this, DownloadMusicService::class.java).apply {
        putExtra(BundleConstant.DATA, download)
    }
    startService(intent)
}

class DownloadMusicService : Service() {

    private var mDownload: Music? = null
    private var NOTIFICATION_ID: Int = 0
    private var mOldPercent: Int = 0
    private var musicId:Int = 0

    private lateinit var mNotificationBuilder: NotificationCompat.Builder

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (null != intent && null == mDownload) {
            NOTIFICATION_ID = UUID.randomUUID().hashCode()
            mDownload = intent.getParcelableExtra(BundleConstant.DATA)
            initNotification()
            startDown()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun initNotification() {
        mNotificationBuilder = NotificationManager.getInstance().createNotification(
            NOTIFICATION_ID,
            "开始下载",
            "开始下载",
            mDownload?.url
        ).apply {
            setOngoing(true)
        }
        startForeground(NOTIFICATION_ID, mNotificationBuilder.build())
    }

    private fun startDown() {
        mDownload?.run {
            val type = if (getMusicUrl().contains("mp3")) "mp3" else "m4a"
            val name = "$author-$title.$type" // 五月天-天使.mp3
            // 保存的地址  APP名字/Download/Music
            val downloadPath = FileDirectoryManager.getDownloadPath() + File.separator + "Music"
            val musics = DataRepository.MUSIC_DAO.findMusics(shareUrl)
            musicId = musics[0].id
            val download = Download(
                musicId = musicId,
                // APP名字/Download/Music/五月天-天使.mp3
                localPath = downloadPath + File.separator + name,
                time = System.currentTimeMillis(),
                status = DownloadStatus.DOWNLOADING
            )
            // 数据库插入一条下载信息
            DataRepository.DOWNLOAD_DAO.insert(download)
            OkHttpUtils.get()
                .url(getMusicUrl())
                .tag(getMusicUrl())
                .build()
                .execute(object : FileCallBack(downloadPath, name) {

                    override fun inProgress(progress: Float, total: Long, id: Int) {
                        // 下载中  通知栏进度的变化
                        val percent = (progress * 100).toInt()
                        if (percent != mOldPercent) {
                            mOldPercent = percent
                            updateProgress(percent)
                        }
                    }

                    override fun onResponse(response: File, id: Int) {
                        // 下载完成
                        updateDownloadDB(DownloadStatus.SUCCESS)
                        updateLocationFile(response)
                        updateNotification("下载完成", true)
                    }

                    override fun onError(call: Call?, e: Exception?, id: Int) {
                        // 下载失败
                        updateDownloadDB(DownloadStatus.FAIL)
                        val error = e?.localizedMessage
                        updateNotification("下载失败", false)
                        ToastUtils.show("下载失败 $error")
                        val file = File(downloadPath, name)
                        if (file.exists()) {
                            file.delete()
                        }
                    }

                })
        }

    }

    private fun updateNotification(title: String, isFinish: Boolean) {
        stopForeground(true)
        val builder = NotificationManager.getInstance().createNotification(
            NOTIFICATION_ID, title, title, mDownload?.url,
            smallIcon = if (isFinish) R.drawable.service_down_finish else R.drawable.service_down
        ).apply {
            setDefaults(Notification.DEFAULT_VIBRATE)
            setAutoCancel(isFinish)
        }
        builder.build().run {
            flags = Notification.FLAG_AUTO_CANCEL
        }
        NotificationManager.getInstance().notify(NOTIFICATION_ID, builder)
        stopSelf()
    }

    private fun updateProgress(percent: Int) {
        mNotificationBuilder.run {
            setContentTitle("下载中")
            setContentText("$percent%")
            setProgress(100, percent, false)
        }
        NotificationManager.getInstance().notify(NOTIFICATION_ID, mNotificationBuilder)
    }

    /**
     * 更新数据库里面下载的状态
     */
    private fun updateDownloadDB(status:Int) {
        DataRepository.DOWNLOAD_DAO.updateDownloadStatus(status,musicId)
    }


    /**
     * 通知系统刷新文件
     */
    private fun updateLocationFile(file: File?) {
        try {
            MediaScannerConnection.scanFile(
                appContext,
                arrayOf(file?.absolutePath),
                null
            ) { path: String?, uri: Uri? ->
                ToastUtils.show("已保存到：$path")
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null


}