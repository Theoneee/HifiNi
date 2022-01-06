package com.theone.music.ui

import android.graphics.Bitmap
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.hjq.toast.ToastUtils
import com.theone.common.constant.BundleConstant
import com.theone.common.ext.*
import com.theone.music.R
import com.theone.music.app.ext.fullSize
import com.theone.music.data.model.MusicInfo
import com.theone.music.data.model.TestAlbum
import com.theone.music.databinding.PageMusicInfoBinding
import com.theone.music.player.PlayerManager
import com.theone.music.viewmodel.MusicInfoViewModel
import com.theone.mvvm.core.base.fragment.BaseCoreFragment
import com.theone.mvvm.core.data.entity.DownloadBean
import com.theone.mvvm.core.ext.showErrorPage
import com.theone.mvvm.core.ext.showLoadingPage
import com.theone.mvvm.core.ext.showSuccessPage
import com.theone.mvvm.core.service.startDownloadService
import com.theone.mvvm.core.util.DownloadUtil
import com.theone.mvvm.core.util.FileDirectoryManager
import com.theone.mvvm.core.util.glide.GlideUtil
import jp.wasabeef.glide.transformations.BlurTransformation
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
        fun newInstance(link: String, name: String): PlayerFragment =
            PlayerFragment().bundle {
                putString(BundleConstant.URL, link)
                putString(BundleConstant.DATA, name)
            }
    }

    private val mLink: String by getValueNonNull(BundleConstant.URL)

    override fun initView(root: View) {
        getTopBar()?.run {
            updateBottomDivider(0, 0, 0, 0)
        }
    }

    override fun initData() {
        mViewModel.link = mLink
    }

    override fun createObserver() {
        mViewModel.getResponseLiveData().observeInFragment(this) {
            showSuccessPage()
            mViewModel.cover.set(it.pic.fullSize())
            getTopBar()?.setTitle(it.title)
            setMediaSource(it)
        }
        mViewModel.getErrorLiveData().observeInFragment(this) {
            showErrorPage(it)
        }

        with(PlayerManager.getInstance()){

           pauseEvent.observe(this@PlayerFragment){
                mViewModel.isPlaying.set(!it)
            }

            playModeEvent.observe(this@PlayerFragment){

            }

            playingMusicEvent.observe(this@PlayerFragment){


            }

            changeMusicEvent.observe(this@PlayerFragment){


            }

        }

    }

    private fun setMediaSource(data: MusicInfo) {
        val artists = TestAlbum.TestArtist().apply {
            name = "UnKnown"
        }
        val music = mutableListOf<TestAlbum.TestMusic>().apply {
            add(TestAlbum.TestMusic().apply {
                musicId = UUID.randomUUID().toString()
                coverImg = data.pic
                title = data.title
                url = data.getMusicUrl()
                artist = artists
            })
        }
        val album = TestAlbum().apply {
            albumId = UUID.randomUUID().toString()
            title = "HiFiNi"
            summary = data.author
            artist = artists
            coverImg = data.pic
            musics = music
        }
        "url = ${data.url}".logI()
        with(PlayerManager.getInstance()) {
            loadAlbum(album,0)
        }
    }

    override fun onLazyInit() {
        onPageReLoad()
    }

    override fun onPageReLoad() {
        mViewModel.requestServer()
    }

    override fun getBindingClick(): Any = ClickProxy()

    inner class ClickProxy{

        fun togglePlayPause(){
            PlayerManager.getInstance().togglePlay()
        }

        fun download(){
            mViewModel.getResponseLiveData().value?.let {
                val type = if(it.url.contains("mp3")) "mp3" else "m4a"
                val download = DownloadBean(
                    it.getImageUrl(),
                    FileDirectoryManager.getDownloadPath() + File.separator + "Music",
                    it.author+"-"+it.title+".$type"
                )
                ToastUtils.show("开始下载")
                mActivity.startDownloadService(download)
            }
        }

    }

}