package com.theone.music.ui.activity

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.RelativeLayout
import androidx.lifecycle.lifecycleScope
import com.qmuiteam.qmui.arch.QMUIFragment
import com.qmuiteam.qmui.kotlin.matchParent
import com.qmuiteam.qmui.kotlin.wrapContent
import com.qmuiteam.qmui.widget.QMUITopBarLayout
import com.theone.common.constant.BundleConstant
import com.theone.common.ext.getValue
import com.theone.common.ext.startActivity
import com.theone.music.R
import com.theone.music.app.ext.toMusic
import com.theone.music.data.model.Music
import com.theone.music.player.PlayerManager
import com.theone.music.ui.fragment.music.LrcFragment
import com.theone.music.ui.fragment.music.PlayerFragment
import com.theone.music.viewmodel.MusicPlayerViewModel
import com.theone.mvvm.core.app.ext.*
import com.theone.mvvm.core.app.ext.qmui.addTab
import com.theone.mvvm.core.app.widge.indicator.SkinPagerTitleView
import com.theone.mvvm.core.base.activity.BaseCoreActivity
import com.theone.mvvm.core.base.adapter.TabFragmentAdapter
import com.theone.mvvm.core.data.entity.QMUIItemBean
import com.theone.mvvm.core.databinding.BaseTabInTitleLayoutBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.lucode.hackware.magicindicator.MagicIndicator
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView

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
 * @date 2022-06-15 09:26
 * @describe TODO
 * @email 625805189@qq.com
 * @remark
 */
class MusicPlayerActivity : BaseCoreActivity<MusicPlayerViewModel, BaseTabInTitleLayoutBinding>() {

    companion object {

        fun startPlay(activity: Activity, music: Music) {
            activity.startActivity<MusicPlayerActivity> {
                putExtra(BundleConstant.DATA, music)
            }
            activity.overridePendingTransition(R.anim.anim_in, 0)
        }

    }

    private val mMusic: Music? by getValue(BundleConstant.DATA)

    /**
     * 获取当前播放的
     */
    private fun getCurrentMusic(): Music {
        return PlayerManager.getInstance().currentPlayingMusic.toMusic()
    }

    override fun onPageReLoad() {
        showLoadingPage()
        getViewModel().requestServer()
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
                        startInit()
                        return
                    }
                    // 不是同一个，就暂停上一个?
                    if (isPlaying) {
                        pauseAudio()
                    }
                }
                // 是否有音频地址，有说明是收藏过来的
                if (music.getMusicUrl().isNotEmpty()) {
                    //直接设置数据
                    setMediaSource(music)
                    return
                }
                // 查询DB
                getViewModel().requestDbMusic()?.let {
                    setMediaSource(it)
                    return
                }
            }
            onPageReLoad()
        }
    }

    override fun createObserver() {
        getViewModel().getRequest().run {
            getResponseLiveData().observe(this@MusicPlayerActivity) {
                setMediaSource(it, true)
            }
            getErrorLiveData().observe(this@MusicPlayerActivity) {
                showErrorPage(it.msg) {
                    onPageReLoad()
                }
            }
        }
    }

    /**
     * 设置播放资源
     * @param data Music
     * @param newData Boolean 是否为请求来的新数据
     */
    private fun setMediaSource(data: Music, newData: Boolean = false) {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                val cacheUrl = PlayerManager.getInstance().getCacheUrl(data.getMusicUrl())
                // 缓存有就直接播放
                if (cacheUrl.startsWith("file")) {
                    loadAlbum(data)
                } else {
                    // 不是新数据才本地还没有缓存就直接重新加载
                    if (!newData) {
                        getViewModel().run {
                            // 本地数据有就重新加载，更新本地数据库，而不是插入一条新数据
                            isReload = data.id > 0
                            requestServer()
                        }
                    } else {
                        loadAlbum(data)
                    }
                }
            }
        }
    }

    private fun loadAlbum(data: Music) {
        PlayerManager.getInstance().loadAlbum(data)
        // 更新音乐信息 最后播放时间
        getViewModel().updateMusicLastPlayDate()
        startInit()
    }

    override fun loaderRegisterView(): View = getRootView()

    /**
     * 这个界面不再设置颜色
     * @return Int?
     */
    override fun getRootBackgroundColor(): Int? = null

    private var mTabs: MutableList<QMUIItemBean> = mutableListOf()
    private var mFragments: MutableList<QMUIFragment> = mutableListOf()

    override fun QMUITopBarLayout.initTopBar() {
        setCenterView(mMagicIndicator)
    }

    private val mPagerAdapter: TabFragmentAdapter by lazy {
        TabFragmentAdapter(supportFragmentManager, mFragments)
    }

    private val mMagicIndicator: MagicIndicator by lazy {
        MagicIndicator(this).apply {
            layoutParams = RelativeLayout.LayoutParams(wrapContent, matchParent)
        }
    }

    private fun startInit() {
        lifecycleScope.launch(Dispatchers.Main) {

            with(mTabs) {
                addTab("歌曲")
                addTab("歌词")
            }
            with(mFragments) {
                add(PlayerFragment())
                add(LrcFragment())
            }
            getDataBinding().run {
                mQMUIViewPager.adapter = mPagerAdapter
                mMagicIndicator.run {
                    navigator = createNavigator()
                    ViewPagerHelper.bind(this, mQMUIViewPager)
                }
            }
            showSuccessPage()
        }
    }

    /**
     * 自定义 CommonNavigator 重写这个方法
     * @return CommonNavigator
     */
    private fun createNavigator(): CommonNavigator = CommonNavigator(this).apply {
        scrollPivotX = 0.65f
        isAdjustMode = false
        adapter = object : CommonNavigatorAdapter() {

            override fun getCount(): Int = mTabs.size

            override fun getTitleView(context: Context, index: Int): IPagerTitleView {
                return SkinPagerTitleView(context).init(
                    index,
                    mTabs,
                    getDataBinding().mQMUIViewPager
                ).initBadgePager(context)
            }

            override fun getIndicator(context: Context): IPagerIndicator? = null

        }
    }

}