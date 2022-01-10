package com.theone.music.ui.fragment

import android.content.Context
import android.widget.RelativeLayout
import com.qmuiteam.qmui.arch.QMUIFragment
import com.qmuiteam.qmui.kotlin.matchParent
import com.qmuiteam.qmui.kotlin.wrapContent
import com.theone.music.R
import com.theone.music.net.NetConstant
import com.theone.music.ui.view.SkinScaleTransitionPagerTitleView
import com.theone.mvvm.base.viewmodel.BaseViewModel
import com.theone.mvvm.core.base.fragment.BaseTabInTitleFragment
import com.theone.mvvm.core.data.entity.QMUITabBean
import com.theone.mvvm.core.ext.init
import com.theone.mvvm.core.ext.initBadgePager
import com.theone.mvvm.core.ext.qmui.addTab
import com.theone.mvvm.core.widge.indicator.SkinPagerTitleView
import net.lucode.hackware.magicindicator.MagicIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView

class MainFragment:BaseTabInTitleFragment<BaseViewModel>() {

    private val mMagicIndicator: MagicIndicator by lazy {
        MagicIndicator(context).apply {
            layoutParams = RelativeLayout.LayoutParams(wrapContent, wrapContent).apply {
                addRule(RelativeLayout.CENTER_IN_PARENT)
                addRule(RelativeLayout.LEFT_OF,R.id.topbar_right_view)
                rightMargin = 20
            }
        }
    }

    override fun getMagicIndicator(): MagicIndicator = mMagicIndicator

    override fun initTopBar() {
        getTopBar()?.run {
            addRightImageButton(R.drawable.mz_titlebar_ic_search_dark,R.id.topbar_right_view).setOnClickListener {
                startFragment(SearchFragment())
            }
            setCenterView(mMagicIndicator)
        }
    }

    override fun getNavTitleView(
        context: Context,
        index: Int,
        tabs: MutableList<QMUITabBean>
    ): IPagerTitleView {
        return SkinScaleTransitionPagerTitleView(context).init(index, tabs, getViewPager()).initBadgePager(context)
    }


    override fun initTabAndFragments(
        tabs: MutableList<QMUITabBean>,
        fragments: MutableList<QMUIFragment>
    ) {

        with(tabs){
            addTab("收藏")
        }

        with(fragments){
            add(CollectionFragment())
        }

        for ((k,v) in NetConstant.CATEGORY){
            tabs.addTab(k)
            fragments.add(MusicItemFragment.newInstance(v))
        }
    }

}