package com.theone.music.ui

import android.widget.RelativeLayout
import com.qmuiteam.qmui.arch.QMUIFragment
import com.qmuiteam.qmui.kotlin.matchParent
import com.qmuiteam.qmui.kotlin.wrapContent
import com.theone.common.ext.invisible
import com.theone.music.R
import com.theone.music.net.NetConstant
import com.theone.mvvm.base.viewmodel.BaseViewModel
import com.theone.mvvm.core.base.fragment.BaseTabInTitleFragment
import com.theone.mvvm.core.base.fragment.BaseTabIndexFragment
import com.theone.mvvm.core.data.entity.QMUITabBean
import com.theone.mvvm.core.ext.qmui.addTab
import net.lucode.hackware.magicindicator.MagicIndicator

class IndexFragment:BaseTabInTitleFragment<BaseViewModel>() {

    private val mMagicIndicator: MagicIndicator by lazy {
        MagicIndicator(context).apply {
            layoutParams = RelativeLayout.LayoutParams(wrapContent, matchParent).apply {
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

    override fun initTabAndFragments(
        tabs: MutableList<QMUITabBean>,
        fragments: MutableList<QMUIFragment>
    ) {

//        with(tabs){
//            addTab("首页")
//        }
//
//        with(fragments){
//            add(HomeFragment())
//        }

        for ((k,v) in NetConstant.CATEGORY){
            tabs.addTab(k)
            fragments.add(MusicItemFragment.newInstance(v))
        }
    }

}