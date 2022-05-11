package com.theone.music.ui.activity

import android.app.Activity
import android.util.Log
import android.util.SparseArray
import android.view.View
import android.widget.SeekBar
import androidx.lifecycle.lifecycleScope
import com.hjq.permissions.OnPermission
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.hjq.toast.ToastUtils
import com.qmuiteam.qmui.arch.SwipeBackLayout
import com.qmuiteam.qmui.arch.SwipeBackLayout.DRAG_DIRECTION_NONE
import com.qmuiteam.qmui.arch.SwipeBackLayout.DRAG_DIRECTION_TOP_TO_BOTTOM
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction
import com.theone.common.callback.OnKeyBackClickListener
import com.theone.common.constant.BundleConstant
import com.theone.common.ext.*
import com.theone.music.BR
import com.theone.music.R
import com.theone.music.app.ext.toMusic
import com.theone.music.app.util.CacheUtil
import com.theone.music.data.model.CollectionEvent
import com.theone.music.data.model.Music
import com.theone.music.data.repository.DataRepository
import com.theone.music.databinding.PageMusicPlayerBinding
import com.theone.music.player.PlayerManager
import com.theone.music.service.startMusicDownloadService
import com.theone.music.ui.view.TheSelectImageView
import com.theone.music.viewmodel.EventViewModel
import com.theone.music.viewmodel.MusicInfoViewModel
import com.theone.mvvm.core.app.ext.showErrorPage
import com.theone.mvvm.core.app.ext.showLoadingPage
import com.theone.mvvm.core.app.ext.showSuccessPage
import com.theone.mvvm.core.base.activity.BaseCoreActivity
import com.theone.mvvm.ext.addParams
import com.theone.mvvm.ext.getAppViewModel
import com.theone.mvvm.ext.qmui.showFailTipsDialog
import com.theone.mvvm.ext.qmui.showMsgDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// ┏┓　  ┏┓
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
        // 首先就是拿到传递过来的数据
        (mMusic ?: getCurrentMusic()).let { music ->
            // 赋值请求地址 https://hifini.com/thread-35837.htm
            getViewModel().link = music.shareUrl
            with(PlayerManager.getInstance()) {
                currentPlayingMusic?.run {
                    // 和当前播放的是同一个，直接拿当前的播放的数据显示
                    if (shareUrl == music.shareUrl) {
                        getViewModel().isSetSuccess.set(true)
                        getCurrentMusic().setMusicInfo()
                        return
                    }
                    // 不是同一个，就暂停上一个?
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
                // 查询DB
                getViewModel().requestDbMusic()?.let {
                    val cacheUrl = getCacheUrl(it.getMusicUrl())
                    setMediaSource(it, cacheUrl.isNotEmpty())
                    return
                }
            }
            getRootView().post {
                onPageReLoad()
            }
        }
    }

    override fun createObserver() {
        // 获取播放信息成功
        getViewModel().getResponseLiveData().observe(this) {
            // 显示成功界面
            showSuccessPage()
            // 重新得到数据后，要刷新收藏里的数据
            if (getViewModel().isReload) {
                getViewModel().isReload = false
                // 从新获取新的播放地址，但是收藏界面的数据并没有更新
                // 避免下一次点击时依然是旧的数据，会造成重复数据加载
                mEvent.dispatchReloadMusic(it)
            }
            setMediaSource(it, true)
        }
        getViewModel().getErrorLiveData().observe(this) {
            showErrorPage(it) {
                onPageReLoad()
            }
        }

        with(PlayerManager.getInstance()) {

            // 暂停事件
            pauseEvent.observe(this@PlayerActivity) {
                getViewModel().isPlaying.set(!it)
            }

            // 当前播放的音乐
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

            // 更改播放音乐
            changeMusicEvent.observe(this@PlayerActivity) {
                mEvent.dispatchPlayMusic(getCurrentMusic())
            }

            // 播放错误
            playErrorEvent.observe(this@PlayerActivity) {
                showFailTipsDialog(it)
            }

        }
    }

    private fun Music.setMusicInfo() {
        getViewModel().let {
            it.isSuccess.set(true)
            it.isCollectionEnable.set(CacheUtil.isLogin())
            it.name.set(title)
            it.author.set(author)
            it.cover.set(pic)
            mEvent.getUserInfoLiveData().value?.let { user ->
                it.requestCollection(user.id, shareUrl)
            }
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
                val cacheUrl = PlayerManager.getInstance().getCacheUrl(data.getMusicUrl())
                // 缓存有就直接播放
                if (cacheUrl.isNullOrEmpty()) {
                    loadAlbum(data)
                } else {
                    // 不是新数据才检查地址是否可行
                    if (!newData && getViewModel().checkUrl(data.getMusicUrl())) {
                        getViewModel().isReload = true
                        getViewModel().requestServer()
                    } else {
                        loadAlbum(data)
                    }
                }
            }
        }
    }

    private fun loadAlbum(data: Music) {
        PlayerManager.getInstance().loadAlbum(data)
        getViewModel().run {
            isSetSuccess.set(true)
            // 更新音乐信息 最后播放时间
            updateMusicLastPlayDate()
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
            // 收藏、添加到喜欢、取消
            // 当前是否有用户登录
            mEvent.getUserInfoLiveData().value?.let { user ->
                // 获取当前播放的数据
                getCurrentMusic().let {
                    // 一个收藏的事件
                    CollectionEvent(isSelected, it).let { event ->
                        // 数据库更新收藏状态
                        getViewModel().toggleCollection(user.id, event)
                        // 分发一个收藏的状态
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


    private fun showFailDialog(
        tips: String,
        actionString: String,
        callback: (() -> Unit?)? = null
    ) {
        showMsgDialog(
            "提示",
            tips,
            rightAction = actionString,
            listener = QMUIDialogAction.ActionListener { dialog, index ->
                dialog.dismiss()
                callback?.invoke()
            },
            prop = QMUIDialogAction.ACTION_PROP_NEGATIVE
        ).run {
            setCanceledOnTouchOutside(false)
            setOnKeyListener(OnKeyBackClickListener())
        }
    }

    /**
     * 请求权限
     *  网络请求的缓存，是放在APP/data目录下的，这个地址是不需要权限申请的
     *
     *
     *
     */
    private fun requestPermission() {
        XXPermissions.with(this)
            .permission(Permission.MANAGE_EXTERNAL_STORAGE)
            .constantRequest()
            .request(object : OnPermission {

                override fun hasPermission(granted: MutableList<String>?, all: Boolean) {
                    startDownload()
                }

                override fun noPermission(denied: MutableList<String>?, quick: Boolean) {
                    if (quick) {
                        showFailDialog(
                            "权限被禁止，请在设置里打开权限",
                            "打开权限"
                        ) {
                            XXPermissions.startPermissionActivity(this@PlayerActivity, denied)
                        }
                    }
                }
            })
    }

    /**
     * 下载：
     *
     *  缺点：
     *  由于时间原因，下载没有做是否已经下载判断
     */
    private fun startDownload() {
        getCurrentMusic().let {
            ToastUtils.show("开始下载")
            startMusicDownloadService(it)
        }
    }

    inner class ClickProxy {

        fun togglePlayPause() {
            PlayerManager.getInstance().togglePlay()
        }

        fun download() {
            requestPermission()
        }

    }

}
