package com.theone.music.ui.activity

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.PARENT_ID
import androidx.fragment.app.FragmentContainerView
import com.qmuiteam.qmui.arch.annotation.DefaultFirstFragment
import com.qmuiteam.qmui.layout.QMUIConstraintLayout
import com.qmuiteam.qmui.widget.QMUIWindowInsetLayout2
import com.theone.common.ext.*
import com.theone.music.R
import com.theone.music.app.ext.toMusic
import com.theone.music.data.model.CollectionEvent
import com.theone.music.data.model.Music
import com.theone.music.data.model.TestAlbum
import com.theone.music.databinding.MusicPlayerLayoutBinding
import com.theone.music.player.PlayerManager
import com.theone.music.ui.fragment.IndexFragment
import com.theone.music.ui.view.TheSelectImageView
import com.theone.music.viewmodel.EventViewModel
import com.theone.music.viewmodel.MusicInfoViewModel
import com.theone.mvvm.base.activity.BaseFragmentActivity
import com.theone.mvvm.ext.getAppViewModel
import com.theone.mvvm.ext.qmui.showFailTipsDialog

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
 * @date 2022-01-04 11:24
 * @describe TODO
 * @email 625805189@qq.com
 * @remark
 *
 *  框架采用的是  单Activity+多 Fragment模式
 *
 *
 *  Activity + Activity +
 *
 *  Activity 一个相框   Fragment 里面的照片  碎片
 *
 */
@DefaultFirstFragment(IndexFragment::class)
class MainActivity : BaseFragmentActivity() {

    // 利用Jetpack Application 级别的 ViewModel+LiveData 进行一个消息分发
    private val mEvent: EventViewModel by lazy { getAppViewModel<EventViewModel>() }
    private val mMusicViewModel: MusicInfoViewModel by viewModels<MusicInfoViewModel>()
    private val playerLayoutHeight by lazy {
        dp2px(60)
    }
    private var mPlayLayout: MusicPlayerLayoutBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createObserve()
    }

    private fun createObserve() {
        with(PlayerManager.getInstance()) {

            // 暂停事件
            pauseEvent.observe(this@MainActivity) {
                mMusicViewModel.isPlaying.set(!it)
            }

            changeMusicEvent.observe(this@MainActivity) { changeMusic ->
                (changeMusic.music as TestAlbum.TestMusic).let { music ->
                    with(mMusicViewModel) {
                        cover.set(music.coverImg)
                        name.set(music.title)
                        author.set(music.author)
                        isSuccess.set(true)
                        mEvent.getUserInfoLiveData().value?.let { user ->
                            requestCollection(user.account, music.shareUrl)
                        }
                    }
                }

                playErrorEvent.observe(this@MainActivity) {
                    showFailTipsDialog(it)
                }

            }
        }
        mEvent.getCollectionLiveData().observe(this) {
            mMusicViewModel.isCollection.set(it.collection)
        }
        mEvent.getUserInfoLiveData().observe(this) {
            // 用户登录、退出后
            // 登录时才能进行点击
            mMusicViewModel.isCollectionEnable.set(it != null)
            if (null == it) {
                // 退出后直接设置为false
                mMusicViewModel.isCollection.set(false)
            } else {
                // 登录后如果当前有播放，查询请求一次是否当前账户已收藏当前播放的歌曲
                getCurrentMusic()?.let { music ->
                    mMusicViewModel.requestCollection(it.account, music.shareUrl)
                }
            }
        }
        // 播放组件是否显示
        mEvent.getPlayWidgetLiveData().observe(this) {
            mPlayLayout?.musicPlayerLayout?.run {
                visible(it)
                val height = if (it) playerLayoutHeight else 0
                if (layoutParams.height == height) {
                    return@run
                }
                layoutParams =
                    ConstraintLayout.LayoutParams(matchParent, height)
                        .apply {
                            startToStart = PARENT_ID
                            endToEnd = PARENT_ID
                            bottomToBottom = PARENT_ID
                        }
            }
        }
        mEvent.getPlayWidgetAlphaLiveData().observe(this) {
            mPlayLayout?.run {
                // 拿到滑动变量然后算出他的高度
                val height = (playerLayoutHeight * (1 - it)).toInt()
                if (musicPlayerLayout.layoutParams.height == height) {
                    return@run
                }
                if (height == 0) {
                    root.gone()
                    return@run
                }
                root.visible()
                musicPlayerLayout.layoutParams =
                    ConstraintLayout.LayoutParams(matchParent, height).apply {
                        startToStart = PARENT_ID
                        endToEnd = PARENT_ID
                        bottomToBottom = PARENT_ID
                    }
            }
        }
    }

    /**
     * 获取当前播放的
     */
    private fun getCurrentMusic(): Music? {
        return PlayerManager.getInstance().currentPlayingMusic?.toMusic()
    }

    inner class ClickProxy : TheSelectImageView.OnSelectChangedListener {

        fun togglePlayPause() {
            PlayerManager.getInstance().togglePlay()
        }

        fun jumpPlayerActivity() {
            getCurrentMusic()?.let {
                MusicPlayerActivity.startPlay(this@MainActivity, it)
            }
        }

        override fun onSelectChanged(isSelected: Boolean) {
            mEvent.getUserInfoLiveData().value?.let { user ->
                getCurrentMusic()?.let {
                    CollectionEvent(isSelected, it).let { event ->
                        mMusicViewModel.toggleCollection(user.account, event)
                        mEvent.dispatchCollectionEvent(event)
                    }
                }
            }
        }

    }

    override fun onCreateRootView(fragmentContainerId: Int): RootView =
        CustomRootView(this, fragmentContainerId)

    inner class CustomRootView(context: Context, fragmentContainerId: Int) :
        RootView(context, fragmentContainerId) {

        private val fragmentContainer: FragmentContainerView =
            FragmentContainerView(context).apply {
                id = fragmentContainerId
            }

        init {
            // DataBinding  这个 Jetpack
            mPlayLayout = MusicPlayerLayoutBinding.inflate(layoutInflater).apply {
                lifecycleOwner = this@MainActivity
                vm = mMusicViewModel
                proxy = ClickProxy()
                (root as QMUIConstraintLayout).run {
                    updateTopDivider(0, 0, 1, getColor(R.color.qmui_config_color_separator))
                }
            }
            // 播放层的布局
            val playerLayoutParams =
                ConstraintLayout.LayoutParams(matchParent, playerLayoutHeight).apply {
                    startToStart = PARENT_ID
                    endToEnd = PARENT_ID
                    bottomToBottom = PARENT_ID
                }

            // 内容层的
            val contentLayoutParams =
                ConstraintLayout.LayoutParams(matchParent, 0).apply {
                    startToStart = PARENT_ID
                    endToEnd = PARENT_ID
                    topToTop = PARENT_ID
                    bottomToTop = R.id.music_player_layout
                }

            val insetLayout = QMUIWindowInsetLayout2(context).apply {
                addView(fragmentContainer, contentLayoutParams)
                addView(mPlayLayout?.root, playerLayoutParams)
            }
            addView(insetLayout, match_match)
        }

        override fun getFragmentContainerView(): FragmentContainerView = fragmentContainer

    }

}