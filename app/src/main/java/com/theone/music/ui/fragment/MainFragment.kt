package com.theone.music.ui.fragment

import android.content.Context
import android.widget.RelativeLayout
import com.qmuiteam.qmui.arch.QMUIFragment
import com.qmuiteam.qmui.widget.QMUITopBarLayout
import com.theone.music.R
import com.theone.music.net.NetConstant
import com.theone.mvvm.base.viewmodel.BaseViewModel
import com.theone.mvvm.core.base.fragment.BaseTabInTitleFragment
import com.theone.mvvm.core.data.entity.QMUIItemBean
import com.theone.mvvm.core.app.ext.qmui.addTab
import com.theone.mvvm.core.app.widge.indicator.SkinScaleTransitionPagerTitleView
import com.theone.mvvm.ext.qmui.addLeftCloseImageBtn
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView

class MainFragment:BaseTabInTitleFragment<BaseViewModel>() {

    override fun generateMagicIndicatorLayoutParams(): RelativeLayout.LayoutParams {
        return super.generateMagicIndicatorLayoutParams().apply {
            addRule(RelativeLayout.CENTER_IN_PARENT)
            addRule(RelativeLayout.RIGHT_OF,R.id.qmui_topbar_item_left_back)
        }
    }

    override fun QMUITopBarLayout.initTopBar() {
        addLeftCloseImageBtn()
        setCenterView(getMagicIndicator())
    }

    override fun getNavIndicator(context: Context): IPagerIndicator? = null

    override fun getPagerTitleView(context: Context): SimplePagerTitleView {
        return SkinScaleTransitionPagerTitleView(context)
    }

    override fun initTabAndFragments(
        tabs: MutableList<QMUIItemBean>,
        fragments: MutableList<QMUIFragment>
    ) {
        for ((k,v) in NetConstant.CATEGORY){
            tabs.addTab(k)
            fragments.add(MusicItemFragment.newInstance(v))
        }
    }

}