package com.theone.music.ui.activity

import android.app.Activity
import android.util.Log
import android.util.SparseArray
import android.view.View
import android.widget.SeekBar
import androidx.lifecycle.lifecycleScope
import com.hjq.toast.ToastUtils
import com.qmuiteam.qmui.arch.SwipeBackLayout
import com.qmuiteam.qmui.arch.SwipeBackLayout.DRAG_DIRECTION_NONE
import com.qmuiteam.qmui.arch.SwipeBackLayout.DRAG_DIRECTION_TOP_TO_BOTTOM
import com.theone.common.constant.BundleConstant
import com.theone.common.ext.*
import com.theone.music.BR
import com.theone.music.R
import com.theone.music.app.ext.toMusic
import com.theone.music.data.model.CollectionEvent
import com.theone.music.data.model.Music
import com.theone.music.data.repository.DataRepository
import com.theone.music.databinding.PageMusicPlayerBinding
import com.theone.music.player.PlayerManager
import com.theone.music.ui.view.TheSelectImageView
import com.theone.music.viewmodel.EventViewModel
import com.theone.music.viewmodel.MusicInfoViewModel
import com.theone.mvvm.core.app.ext.showErrorPage
import com.theone.mvvm.core.app.ext.showLoadingPage
import com.theone.mvvm.core.app.ext.showSuccessPage
import com.theone.mvvm.core.app.util.FileDirectoryManager
import com.theone.mvvm.core.base.activity.BaseCoreActivity
import com.theone.mvvm.core.data.entity.DownloadBean
import com.theone.mvvm.core.service.startDownloadService
import com.theone.mvvm.ext.addParams
import com.theone.mvvm.ext.getAppViewModel
import com.theone.mvvm.ext.qmui.showFailTipsDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.util.*

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
 * @date 2022-01-04 15:25
 * @describe TODO
 * @email 625805189@qq.com
 * @remark
 */
class PlayerActivity :
    BaseCoreActivity<MusicInfoViewModel, PageMusicPlayerBinding>() {

    companion object {

        fun startPlay(activity: Activity, music: Music) {
            activity.startActivity<PlayerActivity> {
                putExtra(BundleConstant.DATA, music)
            }
            activity.overridePendingTransition(R.anim.anim_in, 0)
        }

    }

    private val mEvent: EventViewModel by lazy { getAppViewModel<EventViewModel>() }

    private val mMusic: Music? by getValue(BundleConstant.DATA)

    private var isTrackingTouch: Boolean = false

    override fun loaderRegisterView(): View = getRootView()

    override fun getDragDirection(
        swipeBackLayout: SwipeBackLayout,
        viewMoveAction: SwipeBackLayout.ViewMoveAction,
        downX: Float,
        downY: Float,
        dx: Float,
        dy: Float,
        slopTouch: Float
    ): Int {
        return if (downY < dp2px(500) && dy >= slopTouch) {
            DRAG_DIRECTION_TOP_TO_BOTTOM
        } else DRAG_DIRECTION_NONE
    }

    /**
     * 获取当前播放的
     */
    private fun getCurrentMusic(): Music {
        return getViewModel().getResponseLiveData().value
            ?: PlayerManager.getInstance().currentPlayingMusic.toMusic()
    }

    override fun initView(root: View) {
        (mMusic ?: getCurrentMusic()).let { music ->
            getViewModel().link = music.shareUrl
            with(PlayerManager.getInstance()) {
                currentPlayingMusic?.run {
                    // 和当前播放的是同一个，直接拿当前的播放的数据显示
                    if (shareUrl == music.shareUrl) {
                        getViewModel().isSetSuccess.set(true)
                        getCurrentMusic().setMusicInfo()
                        return
                    }
                    // 不是同一个，就暂停上一个
                    if (isPlaying) {
                        pauseAudio()
                    }
                }
                // 重置所有信息
                getViewModel().reset()
                // 是否有音频地址，有说明是收藏过来的
                if (music.getMusicUrl().isNotEmpty()) {
                    //直接设置数据
                    setMediaSource(music)
                    return
                }
            }
            onPageReLoad()
        }
    }

    override fun createObserver() {
        getViewModel().getResponseLiveData().observe(this) {
            showSuccessPage()
            // 重新得到数据后，要刷新收藏里的数据
            if (getViewModel().isReload) {
                getViewModel().isReload = false
                mEvent.dispatchReloadMusic(it)
            }
            setMediaSource(it, true)
        }
        getViewModel().getErrorLiveData().observe(this) {
            showErrorPage(it)
        }

        with(PlayerManager.getInstance()) {

            pauseEvent.observe(this@PlayerActivity) {
                getViewModel().isPlaying.set(!it)
            }

            playingMusicEvent.observe(this@PlayerActivity) {
                // 拖动进度条时不再进行设值
                if (isTrackingTouch) {
                    return@observe
                }
                getViewModel().run {
                    // 只有在设置了音乐数据后才能设置播放信息，避免被上一首的播放信息污染
                    if (!isSetSuccess.get()) {
                        return@observe
                    }
                    max.set(it.duration)
                    progress.set(it.playerPosition)
                    nowTime.set(it.nowTime)
                    allTime.set(it.allTime)
                }
            }

            changeMusicEvent.observe(this@PlayerActivity) {
                mEvent.dispatchPlayMusic(getCurrentMusic())
            }

            playErrorEvent.observe(this@PlayerActivity) {
                showFailTipsDialog(it)
            }

        }

    }

    private fun Music.setMusicInfo() {
        getViewModel().let {
            it.isSuccess.set(true)
            it.name.set(title)
            it.author.set(author)
            it.cover.set(pic)
            it.requestCollection(shareUrl)
        }
    }

    /**
     * 设置播放资源
     * @param data Music
     * @param newData Boolean 是否为请求来的新数据
     */
    private fun setMediaSource(data: Music, newData: Boolean = false) {
        data.setMusicInfo()
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                // 不是新数据才检查地址是否可行
                if (!newData && DataRepository.INSTANCE.checkUrl(data.getMusicUrl())) {
                    Log.e(TAG, "setMediaSource: ${Thread.currentThread().name}")
                    getViewModel().isReload = true
                    getViewModel().requestServer()
                    return@withContext
                }
                val album = DataRepository.INSTANCE.createAlbum(data)
                PlayerManager.getInstance().loadAlbum(album, 0)
                getViewModel().isSetSuccess.set(true)
            }
        }
    }

    override fun onPageReLoad() {
        showLoadingPage()
        getViewModel().requestServer()
    }

    override fun SparseArray<Any>.applyBindingParams() {
        addParams(BR.listener, ListenerProxy())
    }

    override fun getBindingClick(): Any = ClickProxy()


    inner class ListenerProxy : TheSelectImageView.OnSelectChangedListener,
        SeekBar.OnSeekBarChangeListener {

        override fun onSelectChanged(isSelected: Boolean) {
            mEvent.getUserInfoLiveData().value?.let { user ->
                getCurrentMusic().let {
                    CollectionEvent(isSelected, it).let { event ->
                        getViewModel().toggleCollection(user.id, event)
                        mEvent.dispatchCollectionEvent(event)
                    }
                }
            }
        }

        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            if (fromUser) {
                getViewModel().nowTime.set(PlayerManager.getInstance().getTrackTime(progress))
            }
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {
            isTrackingTouch = true
        }

        override fun onStopTrackingTouch(seekBar: SeekBar?) {
            isTrackingTouch = false
            seekBar?.let {
                PlayerManager.getInstance().setSeek(it.progress)
            }
        }

    }

    inner class ClickProxy {

        fun togglePlayPause() {
            PlayerManager.getInstance().togglePlay()
        }

        fun download() {
            getCurrentMusic().let {
                val type = if (it.url.contains("mp3")) "mp3" else "m4a"
                val download = DownloadBean(
                    it.getMusicUrl(),
                    FileDirectoryManager.getDownloadPath() + File.separator + "Music",
                    it.author + "-" + it.title + ".$type"
                )
                ToastUtils.show("开始下载")
                startDownloadService(download)
            }
        }

    }

}
