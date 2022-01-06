package com.theone.music.ui

import android.util.SparseArray
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.hjq.toast.ToastUtils
import com.theone.common.constant.BundleConstant
import com.theone.common.ext.*
import com.theone.lover.data.room.AppDataBase
import com.theone.music.BR
import com.theone.music.app.ext.fullSize
import com.theone.music.data.model.MusicInfo
import com.theone.music.data.model.TestAlbum
import com.theone.music.data.repository.DataRepository
import com.theone.music.data.room.MusicDao
import com.theone.music.databinding.PageMusicInfoBinding
import com.theone.music.player.PlayerManager
import com.theone.music.ui.view.TheSelectImageView
import com.theone.music.viewmodel.MusicInfoViewModel
import com.theone.mvvm.core.base.fragment.BaseCoreFragment
import com.theone.mvvm.core.data.entity.DownloadBean
import com.theone.mvvm.core.ext.showErrorPage
import com.theone.mvvm.core.ext.showLoadingPage
import com.theone.mvvm.core.ext.showSuccessPage
import com.theone.mvvm.core.service.startDownloadService
import com.theone.mvvm.core.util.FileDirectoryManager
import com.theone.mvvm.ext.addParams
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
class PlayerFragment private constructor() :
    BaseCoreFragment<MusicInfoViewModel, PageMusicInfoBinding>() {

    companion object {

        fun newInstance(musicInfo: MusicInfo): PlayerFragment =
            PlayerFragment().bundle {
                putParcelable(BundleConstant.DATA, musicInfo)
            }
    }

    private val mMusicInfo: MusicInfo by getValueNonNull(BundleConstant.DATA)

    override fun initView(root: View) {
        getTopBar()?.run {
            updateBottomDivider(0, 0, 0, 0)
        }
    }

    override fun initData() {
        with(mMusicInfo) {
            if (url.isEmpty()) {
                mViewModel.link = mMusicInfo.shareUrl
            } else {
                setMediaSource(this)
            }
        }
        setMediaSource(mMusicInfo)
    }

    override fun createObserver() {
        mViewModel.getResponseLiveData().observeInFragment(this) {
            showSuccessPage()
            setMediaSource(it)
        }
        mViewModel.getErrorLiveData().observeInFragment(this) {
            showErrorPage(it)
        }

        with(PlayerManager.getInstance()) {

            pauseEvent.observe(this@PlayerFragment) {
                mViewModel.isPlaying.set(!it)
            }

            playModeEvent.observe(this@PlayerFragment) {

            }

            playingMusicEvent.observe(this@PlayerFragment) {


            }

            changeMusicEvent.observe(this@PlayerFragment) {


            }

        }

    }

    private fun setMusicInfo(cover:String,title:String,shareUrl:String){
        mViewModel.isSuccess.set(true)
        mViewModel.cover.set(cover)
        getTopBar()?.setTitle(title)
        mViewModel.requestCollection(shareUrl)
    }

    private fun setMediaSource(data: MusicInfo) {
        with(PlayerManager.getInstance()) {
            if (null != currentPlayingMusic ) {
                with(currentPlayingMusic){
                    if(shareUrl == data.shareUrl){
                        setMusicInfo(coverImg,title,shareUrl)
                        return
                    }
                }
            }
            if(data.getMusicUrl().isEmpty()){
                return
            }
        }
        setMusicInfo(data.pic.fullSize(),data.title,data.shareUrl)
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                val album = DataRepository.INSTANCE.createAlbum(data)
                PlayerManager.getInstance().loadAlbum(album, 0)
            }
        }
    }

    override fun onLazyInit() {
        if(!mViewModel.isSuccess.get())
            onPageReLoad()
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


    inner class SelectListener : TheSelectImageView.OnSelectChangedListener {

        override fun onSelectChanged(isSelected: Boolean) {
            mViewModel.toggleCollection(isSelected,PlayerManager.getInstance().currentPlayingMusic)
        }

    }

    inner class ClickProxy {

        fun togglePlayPause() {
            PlayerManager.getInstance().togglePlay()
        }

        fun download() {
            mViewModel.getResponseLiveData().value?.let {
                val type = if (it.url.contains("mp3")) "mp3" else "m4a"
                val download = DownloadBean(
                    it.getMusicUrl(),
                    FileDirectoryManager.getDownloadPath() + File.separator + "Music",
                    it.author + "-" + it.title + ".$type"
                )
                ToastUtils.show("开始下载")
                mActivity.startDownloadService(download)
            }
        }

    }

}