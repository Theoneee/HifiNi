package com.theone.music.ui.fragment.user

import android.content.Context
import com.qmuiteam.qmui.arch.QMUIFragment
import com.qmuiteam.qmui.widget.QMUITopBarLayout
import com.theone.common.constant.BundleConstant
import com.theone.common.ext.newFragment
import com.theone.music.R
import com.theone.mvvm.base.viewmodel.BaseViewModel
import com.theone.mvvm.core.data.entity.QMUIItemBean
import com.theone.mvvm.core.app.ext.qmui.addTab
import com.theone.mvvm.core.app.widge.indicator.SkinScaleTransitionPagerTitleView
import com.theone.mvvm.core.base.fragment.BaseTabInTitleFragment
import com.theone.mvvm.ext.qmui.addLeftCloseImageBtn
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView


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
 * @date 2021/3/15 0015
 * @describe TODO
 * @email 625805189@qq.com
 * @remark
 */
class LoginRegisterFragment : BaseTabInTitleFragment<BaseViewModel>() {

    override fun QMUITopBarLayout.initTopBar() {
        addLeftCloseImageBtn(R.drawable.mz_comment_titlebar_ic_close_dark)
        updateBottomDivider(0, 0, 0, 0)
        setCenterView(getMagicIndicator())
    }

    override fun initTabAndFragments(
        tabs: MutableList<QMUIItemBean>,
        fragments: MutableList<QMUIFragment>
    ) {
        with(tabs) {
            addTab("登录")
            addTab("注册")
        }


        with(fragments) {
            add(LoginRegisterItemFragment.newInstant(false))
            add(
                newFragment<LoginRegisterItemFragment> {
                    putBoolean(BundleConstant.TYPE, true)
                }
            )
        }
    }

    override fun getPagerTitleView(context: Context): SimplePagerTitleView {
        return SkinScaleTransitionPagerTitleView(context)
    }

    override fun getNavIndicator(context: Context): IPagerIndicator? = null

    /**
     * 更改进出动画效果 QMUIFragment提供
     */
    override fun onFetchTransitionConfig(): TransitionConfig = SCALE_TRANSITION_CONFIG

}