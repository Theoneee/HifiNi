package com.theone.music.ui

import android.media.MediaPlayer
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.theone.common.constant.BundleConstant
import com.theone.common.ext.bundle
import com.theone.common.ext.getValueNonNull
import com.theone.music.app.ext.fullSize
import com.theone.music.data.model.MusicInfo
import com.theone.music.data.model.TestAlbum
import com.theone.music.databinding.PageMusicInfoBinding
import com.theone.music.player.PlayerManager
import com.theone.music.viewmodel.MusicInfoViewModel
import com.theone.mvvm.core.base.fragment.BaseCoreFragment
import com.theone.mvvm.core.ext.showErrorPage
import com.theone.mvvm.core.ext.showLoadingPage
import com.theone.mvvm.core.ext.showSuccessPage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
class MusicInfoFragment private constructor() :
    BaseCoreFragment<MusicInfoViewModel, PageMusicInfoBinding>() {

    companion object {
        fun newInstance(link: String,name:String): MusicInfoFragment = MusicInfoFragment().bundle {
            putString(BundleConstant.URL, link)
            putString(BundleConstant.DATA, name)
        }
    }

    private val mLink: String by getValueNonNull(BundleConstant.URL)
    private val mName: String by getValueNonNull(BundleConstant.DATA)


    private val mMediaPlayer: MediaPlayer by lazy {
        MediaPlayer()
    }


    override fun initView(root: View) {
        getTopBar()?.run {
            setTitle(mName)
            updateBottomDivider(0,0,0,0)
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
    }

    private fun setMediaSource(data: MusicInfo) {
        PlayerManager.getInstance().loadAlbum(TestAlbum().apply {
            musics = arrayListOf(TestAlbum.TestMusic().apply {
                coverImg = data.pic
                title = data.title
                url = data.url
            })
        })
//        lifecycleScope.launch {
//            withContext(Dispatchers.IO){
//
//            }
//        }
    }


    override fun onLazyInit() {
        onPageReLoad()
    }

    override fun onPageReLoad() {
        showLoadingPage()
        mViewModel.requestServer()
    }

}