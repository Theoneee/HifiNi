package com.theone.music.ui.fragment

import android.view.View
import com.theone.mvvm.core.base.fragment.BaseTabInTitleFragment
import com.theone.mvvm.base.viewmodel.BaseViewModel
import com.theone.music.viewmodel.EventViewModel
import com.qmuiteam.qmui.alpha.QMUIAlphaImageButton
import android.widget.RelativeLayout
import com.theone.music.R
import com.qmuiteam.qmui.util.QMUIDisplayHelper
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.theone.mvvm.core.data.entity.QMUIItemBean
import com.qmuiteam.qmui.arch.QMUIFragment
import com.qmuiteam.qmui.widget.QMUITopBarLayout
import com.theone.music.viewmodel.MainViewModel
import com.theone.mvvm.core.app.ext.qmui.addTab
import com.theone.mvvm.ext.getAppViewModel
import com.theone.mvvm.ext.qmui.showSuccessTipsDialog

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
 * @date 2022-04-06 11:23
 * @describe TODO
 * @email 625805189@qq.com
 * @remark
 */
class IndexFragment : BaseTabInTitleFragment<MainViewModel>() {

    private val mEvent: EventViewModel by lazy { getAppViewModel<EventViewModel>() }
    private var mSearchBtn: QMUIAlphaImageButton? = null

    override fun isNeedChangeStatusBarMode(): Boolean =  true

    override fun translucentFull(): Boolean = true

    override fun generateMagicIndicatorLayoutParams(): RelativeLayout.LayoutParams {
        return super.generateMagicIndicatorLayoutParams().apply {
            addRule(RelativeLayout.ALIGN_PARENT_START)
            addRule(RelativeLayout.LEFT_OF, R.id.topbar_right_view)
            bottomMargin = QMUIDisplayHelper.dp2px(context, 10)
        }
    }

    override fun QMUITopBarLayout.initTopBar() {
        mSearchBtn =addRightImageButton(
            R.drawable.mz_titlebar_ic_search_dark,
            R.id.topbar_right_view
        ).apply {
            setOnClickListener(View.OnClickListener { startFragment(SearchFragment()) })
        }
        setBackgroundAlpha(0)
        setCenterView(getMagicIndicator())
    }

    override fun initView(root: View) {
        super.initView(root)
        getViewPager().currentItem = 1
        // 处理滑动的
        getViewPager().addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                // 当是第二个滑动的时候才进行处理
                if (position == 1) {
                    mEvent.dispatchPlayWidgetAlphaEvent(positionOffset)
                }
            }

            override fun onPageSelected(position: Int) {}
            override fun onPageScrollStateChanged(state: Int) {}
        })
    }

    override fun initTabAndFragments(
        tabs: MutableList<QMUIItemBean>,
        fragments: MutableList<QMUIFragment>
    ) {
        with(tabs){
            addTab("我的")
            addTab("乐库")
            addTab("MV")
        }
        with(fragments){
            add(MineFragment())
            add(MusicRepositoryFragment())
            add(MVFragment())
        }
    }

    override fun createObserver() {
        super.createObserver()
        getViewModel().getResponseLiveData().observe(this){
            showSuccessTipsDialog(it)
        }
        mEvent.getPlayWidgetAlphaLiveData().observe(this) {
                show: Float ->
            mSearchBtn?.setImageResource(if (show > 0.6) R.drawable.mz_titlebar_ic_search_light else R.drawable.mz_titlebar_ic_search_dark) }
    }

}