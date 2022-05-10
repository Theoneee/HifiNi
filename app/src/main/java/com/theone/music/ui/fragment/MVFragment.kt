package com.theone.music.ui.fragment

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.shuyu.gsyvideoplayer.video.base.GSYVideoView
import com.theone.music.R
import com.theone.music.data.model.MV
import com.theone.music.player.PlayerManager
import com.theone.music.ui.adapter.MvAdapter
import com.theone.music.ui.view.VideoPlayer
import com.theone.music.ui.view.OnViewPagerListener
import com.theone.music.ui.view.PagerLayoutManager
import com.theone.music.viewmodel.EventViewModel
import com.theone.music.viewmodel.MvViewModel
import com.theone.mvvm.core.data.enum.LayoutManagerType
import com.theone.mvvm.ext.getAppViewModel

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
 * @date 2022-04-08 09:09
 * @describe TODO
 * @email 625805189@qq.com
 * @remark
 */
class MVFragment : BasePagerFragment<MV, MvViewModel>() {

    val mEvent: EventViewModel by lazy { getAppViewModel<EventViewModel>() }

    private var mPlayPosition = 0

    private var isPageVisible: Boolean = false

    override fun isStatusBarLightMode(): Boolean = false

    override fun isNeedChangeStatusBarMode(): Boolean = true

    override fun translucentFull(): Boolean = true

    override fun isLazyLoadData(): Boolean = false

    override fun getLayoutManagerType(): LayoutManagerType = LayoutManagerType.GRID

    override fun createAdapter(): BaseQuickAdapter<MV, *> = MvAdapter()

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {

    }

    // 这个是让一个Item充满父布局的
    override fun getLayoutManager(): RecyclerView.LayoutManager =
        PagerLayoutManager(context, LinearLayoutManager.VERTICAL).apply {
            setOnViewPagerListener(object : OnViewPagerListener {

                override fun onInitComplete(view: View?) {
                    startPlayVideo(mPlayPosition, view)
                }

                override fun onPageRelease(isNext: Boolean, position: Int, view: View?) {
                    view?.findViewById<VideoPlayer>(R.id.video_player)?.onVideoPause()
                }

                override fun onPageSelected(position: Int, isBottom: Boolean, view: View?) {
                    if (mPlayPosition != position) {
                        startPlayVideo(position, view)
                    }
                }
            })
        }

    private fun startPlayVideo(position: Int, view: View?) {
        mPlayPosition = position
        if (!isPageVisible) {
            return
        }
        view?.findViewById<VideoPlayer>(R.id.video_player)?.let {
            if (it.currentState == GSYVideoView.CURRENT_STATE_PAUSE) {
                it.onVideoResume()
            } else {
                it.startPlayLogic()
            }
        }
    }

    override fun setRefreshLayoutEnabled(enabled: Boolean) {
        super.setRefreshLayoutEnabled(false)
    }

    private var isPlaying = false

    // 当界面显示的时候
    override fun onLazyResume() {
        super.onLazyResume()
        isPageVisible = true
        // 发一个通知
        mEvent.dispatchPlayWidgetEvent(false)
        (mAdapter as MvAdapter).onResume()
        if (PlayerManager.getInstance().isPlaying) {
            isPlaying = true
            PlayerManager.getInstance().pauseAudio()
        }
    }

    // 当界面不可见的时候
    override fun onPause() {
        if (isPlaying) {
            PlayerManager.getInstance().resumeAudio()
        }
        isPageVisible = false
        mEvent.dispatchPlayWidgetEvent(true)
        super.onPause()
        (mAdapter as MvAdapter).onPause()
    }

    @Override
    override fun onDestroy() {
        (mAdapter as MvAdapter).onDestroy()
        super.onDestroy()
    }

}