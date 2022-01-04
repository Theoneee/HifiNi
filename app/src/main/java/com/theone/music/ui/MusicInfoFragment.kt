package com.theone.music.ui

import android.media.MediaPlayer
import android.view.View
import com.theone.common.constant.BundleConstant
import com.theone.common.ext.bundle
import com.theone.common.ext.getValueNonNull
import com.theone.music.app.ext.fullSize
import com.theone.music.databinding.PageMusicInfoBinding
import com.theone.music.viewmodel.MusicInfoViewModel
import com.theone.mvvm.core.base.fragment.BaseCoreFragment
import com.theone.mvvm.core.ext.showErrorPage
import com.theone.mvvm.core.ext.showLoadingPage
import com.theone.mvvm.core.ext.showSuccessPage

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
        fun newInstance(link: String): MusicInfoFragment = MusicInfoFragment().bundle {
            putString(BundleConstant.DATA, link)
        }
    }

    private val mLink: String by getValueNonNull(BundleConstant.DATA)

    private val mMediaPlayer:MediaPlayer by lazy {
        MediaPlayer()
    }

    override fun initData() {
        mViewModel.link = mLink
    }

    override fun createObserver() {
        mViewModel.getResponseLiveData().observeInFragment(this){
            showSuccessPage()
            mViewModel.cover.set(it.pic.fullSize())
            setMediaSource(it.getMusicUrl())
        }
        mViewModel.getErrorLiveData().observeInFragment(this){
            showErrorPage(it)
        }
    }

    private fun setMediaSource(url:String){
        mMediaPlayer.run {
            setDataSource(url)
            prepare()
            setOnPreparedListener {
                start()
            }
        }
    }

    override fun initView(root: View) {

    }

    override fun onLazyInit() {
        showLoadingPage()
        mViewModel.requestServer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mMediaPlayer.run {
            stop()
            release()
        }
    }

}