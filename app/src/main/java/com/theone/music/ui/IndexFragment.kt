package com.theone.music.ui

import com.qmuiteam.qmui.arch.QMUIFragment
import com.theone.mvvm.base.viewmodel.BaseViewModel
import com.theone.mvvm.core.base.fragment.BaseTabIndexFragment
import com.theone.mvvm.core.data.entity.QMUITabBean
import com.theone.mvvm.core.ext.qmui.addTab

class IndexFragment:BaseTabIndexFragment<BaseViewModel>() {

    override fun initTabAndFragments(
        tabs: MutableList<QMUITabBean>,
        fragments: MutableList<QMUIFragment>
    ) {

        with(tabs){
            addTab("首页")
        }

        with(fragments){
            add(HomeFragment())
        }

    }

}