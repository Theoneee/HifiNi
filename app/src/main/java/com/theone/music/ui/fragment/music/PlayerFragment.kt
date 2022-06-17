package com.theone.music.ui.fragment.music

import android.util.Log
import android.util.SparseArray
import android.view.View
import android.widget.SeekBar
import com.hjq.permissions.OnPermission
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.hjq.toast.ToastUtils
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction
import com.theone.common.callback.OnKeyBackClickListener
import com.theone.music.BR
import com.theone.music.app.ext.toMusic
import com.theone.music.app.util.CacheUtil
import com.theone.music.data.model.CollectionEvent
import com.theone.music.data.model.Music
import com.theone.music.databinding.PageMusicPlayerBinding
import com.theone.music.player.PlayerManager
import com.theone.music.service.startMusicDownloadService
import com.theone.music.ui.view.TheSelectImageView
import com.theone.music.viewmodel.EventViewModel
import com.theone.music.viewmodel.MusicInfoViewModel
import com.theone.mvvm.core.base.fragment.BaseCoreFragment
import com.theone.mvvm.ext.addParams
import com.theone.mvvm.ext.getAppViewModel
import com.theone.mvvm.ext.qmui.showFailTipsDialog
import com.theone.mvvm.ext.qmui.showMsgDialog

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
class PlayerFragment :
    BaseCoreFragment<MusicInfoViewModel, PageMusicPlayerBinding>() {

    private val mEvent: EventViewModel by lazy { getAppViewModel<EventViewModel>() }

    private var isTrackingTouch: Boolean = false

    override fun loaderRegisterView(): View = getRootView()

    private val mMusic:Music by lazy {
        PlayerManager.getInstance().currentPlayingMusic.toMusic()
    }

    /**
     * 获取当前播放的
     */
    private fun getCurrentMusic() = mMusic

    override fun initView(root: View) {
        getCurrentMusic().setMusicInfo()
    }

    override fun createObserver() {
        with(PlayerManager.getInstance()) {
            // 暂停事件
            pauseEvent.observe(this@PlayerFragment) {
                getViewModel().isPlaying.set(!it)
            }
            // 当前播放的音乐
            playingMusicEvent.observe(this@PlayerFragment) {
                // 拖动进度条时不再进行设值
                if (isTrackingTouch) {
                    return@observe
                }
                getViewModel().run {
                    max.set(it.duration)
                    progress.set(it.playerPosition)
                    nowTime.set(it.nowTime)
                    allTime.set(it.allTime)
                }
            }
            // 播放错误
            playErrorEvent.observe(this@PlayerFragment) {
                showFailTipsDialog(it)
            }
        }
    }

    private fun Music.setMusicInfo() {
        getViewModel().let {
            it.isSuccess.set(true)
            it.isSetSuccess.set(true)
            it.isCollectionEnable.set(CacheUtil.isLogin())
            it.name.set(name)
            it.author.set(singer)
            it.cover.set(cover)
            mEvent.getUserInfoLiveData().value?.let { user ->
                it.requestCollection(user.account, shareUrl)
            }
        }
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
                        getViewModel().toggleCollection(user.account, event)
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
        mActivity.showMsgDialog(
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
     */
    private fun requestPermission() {
        XXPermissions.with(mActivity)
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
                            XXPermissions.startPermissionActivity(mActivity, denied)
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
            mActivity.startMusicDownloadService(it)
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
