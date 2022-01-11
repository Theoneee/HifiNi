package com.theone.music.ui.activity

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import androidx.activity.viewModels
import androidx.fragment.app.FragmentContainerView
import com.hjq.permissions.OnPermission
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.qmuiteam.qmui.arch.annotation.DefaultFirstFragment
import com.qmuiteam.qmui.layout.QMUIConstraintLayout
import com.qmuiteam.qmui.widget.QMUIWindowInsetLayout
import com.theone.common.ext.*
import com.theone.music.R
import com.theone.music.data.model.CollectionEvent
import com.theone.music.data.model.Music
import com.theone.music.data.model.TestAlbum
import com.theone.music.databinding.MusicPlayerLayoutBinding
import com.theone.music.player.PlayerManager
import com.theone.music.ui.fragment.MainFragment
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
 */
@DefaultFirstFragment(MainFragment::class)
class MainActivity : BaseFragmentActivity() {

    private val mEvent: EventViewModel by lazy { getAppViewModel<EventViewModel>() }
    private val mMusicViewModel: MusicInfoViewModel by viewModels<MusicInfoViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermission()
        createObserve()
    }

    private fun createObserve(){
        with(PlayerManager.getInstance()) {

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
                        requestCollection(music.shareUrl)
                    }
                }

                playErrorEvent.observe(this@MainActivity){
                    showFailTipsDialog(it)
                }

            }
        }
        mEvent.getCollectionLiveData().observeInActivity(this){
            mMusicViewModel.isCollection.set(it.collection)
        }
    }

    /**
     * 请求权限
     */
    private fun requestPermission() {
        XXPermissions.with(this)
            .permission(Permission.MANAGE_EXTERNAL_STORAGE)
            .constantRequest()
            .request(object : OnPermission {

                override fun hasPermission(granted: MutableList<String>?, all: Boolean) {

                }

                override fun noPermission(denied: MutableList<String>?, quick: Boolean) {
                }
            })
    }

    /**
     * 获取当前播放的
     */
    private fun getCurrentMusic(): Music? {
        return PlayerManager.getInstance().currentPlayingMusic?.let {
            Music(it)
        }
    }

    inner class ClickProxy : TheSelectImageView.OnSelectChangedListener {

        fun togglePlayPause() {
            PlayerManager.getInstance().togglePlay()
        }

        fun jumpPlayerActivity() {
            getCurrentMusic()?.let {
                PlayerActivity.startPlay(this@MainActivity, it)
            }
        }

        override fun onSelectChanged(isSelected: Boolean) {
            getCurrentMusic()?.let {
                CollectionEvent(isSelected, it).let { event ->
                    mMusicViewModel.toggleCollection(event)
                    mEvent.dispatchCollectionEvent(event)
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
            val mBinding = MusicPlayerLayoutBinding.inflate(layoutInflater).apply {
                lifecycleOwner = this@MainActivity
                vm = mMusicViewModel
                proxy = ClickProxy()
                (root as QMUIConstraintLayout).run {
//                    val radius = dp2px(20)
//                    setRadius(radius,QMUILayoutHelper.HIDE_RADIUS_SIDE_BOTTOM)
                    updateTopDivider(0,0,1,getColor(R.color.qmui_config_color_separator))
                }
            }

            val insetLayout = QMUIWindowInsetLayout(context).apply {
                val playerLayoutHeight = dp2px(60)
                addView(fragmentContainer, LayoutParams(matchParent, matchParent).apply {
                    setMargins(0,0,0,playerLayoutHeight)
                })
                addView(mBinding.root, LayoutParams(matchParent,playerLayoutHeight).apply {
                    gravity = Gravity.BOTTOM
                })
            }
            addView(insetLayout, match_match)
        }

        override fun getFragmentContainerView(): FragmentContainerView = fragmentContainer

    }

}