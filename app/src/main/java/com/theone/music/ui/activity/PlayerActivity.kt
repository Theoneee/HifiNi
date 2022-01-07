package com.theone.music.ui.activity

import android.app.Activity
import android.util.SparseArray
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.hjq.toast.ToastUtils
import com.theone.common.constant.BundleConstant
import com.theone.common.ext.*
import com.theone.music.BR
import com.theone.music.R
import com.theone.music.data.model.CollectionEvent
import com.theone.music.data.model.Music
import com.theone.music.data.repository.DataRepository
import com.theone.music.databinding.PageMusicPlayerBinding
import com.theone.music.player.PlayerManager
import com.theone.music.ui.view.TheSelectImageView
import com.theone.music.viewmodel.EventViewModel
import com.theone.music.viewmodel.MusicInfoViewModel
import com.theone.mvvm.core.base.activity.BaseCoreActivity
import com.theone.mvvm.core.data.entity.DownloadBean
import com.theone.mvvm.core.ext.showErrorPage
import com.theone.mvvm.core.ext.showLoadingPage
import com.theone.mvvm.core.ext.showSuccessPage
import com.theone.mvvm.core.service.startDownloadService
import com.theone.mvvm.core.util.FileDirectoryManager
import com.theone.mvvm.ext.addParams
import com.theone.mvvm.ext.getAppViewModel
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

    override fun initView(root: View) {
        getTopBar()?.run {
            updateBottomDivider(0, 0, 0, 0)
        }
    }

    override fun initData() {
        (mMusic ?: getCurrentMusic()).let { music ->
            with(PlayerManager.getInstance()) {
                currentPlayingMusic?.run {
                    // 和当前播放的是同一个，直接拿当前的播放的数据显示
                    if (shareUrl == music.shareUrl) {
                        getCurrentMusic().setMusicInfo()
                        return
                    }
                }
                // 不是同一个，就暂停上一个，重置信息
                if (isPlaying) {
                    pauseAudio()
                    reset()
                }
                // 是否有音频地址，有说明是收藏过来的，设置新的数据
                if (music.getMusicUrl().isNotEmpty()) {
                    setMediaSource(music)
                    return
                }
            }
            mViewModel.link = music.shareUrl
            onPageReLoad()
        }

    }

    override fun createObserver() {
        mViewModel.getResponseLiveData().observeInActivity(this) {
            showSuccessPage()
            setMediaSource(it)
        }
        mViewModel.getErrorLiveData().observeInActivity(this) {
            showErrorPage(it)
        }

        with(PlayerManager.getInstance()) {

            pauseEvent.observe(this@PlayerActivity) {
                mViewModel.isPlaying.set(!it)
            }

            playingMusicEvent.observe(this@PlayerActivity) {
                mViewModel.run {
                    max.set(it.duration)
                    nowTime.set(it.nowTime)
                    allTime.set(it.allTime)
                    progress.set(it.playerPosition)
                }
            }

            changeMusicEvent.observe(this@PlayerActivity) {
                mEvent.dispatchPlayMusic(getCurrentMusic())
            }

        }

    }

    private fun reset() {
        mViewModel.run {
            max.set(0)
            progress.set(0)
            nowTime.set("")
            allTime.set("")
            isCollection.set(false)
        }
    }

    private fun Music.setMusicInfo() {
        mViewModel.let {
            it.isSuccess.set(true)
            it.name.set(title)
            it.author.set(author)
            it.cover.set(pic)
            reset()
            it.requestCollection(shareUrl)
        }
    }

    private fun setMediaSource(data: Music) {
        data.setMusicInfo()
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                val album = DataRepository.INSTANCE.createAlbum(data)
                PlayerManager.getInstance().loadAlbum(album, 0)
            }
        }
    }

    override fun onPageReLoad() {
        showLoadingPage()
        mViewModel.requestServer()
    }

    override fun createBindingParams(bindingParams: SparseArray<Any>) {
        super.createBindingParams(bindingParams)
        bindingParams.addParams(BR.listener, SelectListener())
    }

    override fun getBindingClick(): Any = ClickProxy()

    /**
     * 获取当前播放的
     */
    private fun getCurrentMusic(): Music {
        return mViewModel.getResponseLiveData().value
            ?: Music(PlayerManager.getInstance().currentPlayingMusic)
    }

    inner class SelectListener : TheSelectImageView.OnSelectChangedListener {

        override fun onSelectChanged(isSelected: Boolean) {
            getCurrentMusic().let {
                CollectionEvent(isSelected, it).let { event ->
                    mViewModel.toggleCollection(event)
                    mEvent.dispatchCollectionEvent(event)
                }
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