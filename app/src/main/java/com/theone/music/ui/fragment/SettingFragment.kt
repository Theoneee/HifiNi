package com.theone.music.ui.fragment

import android.view.View
import com.qmuiteam.qmui.util.QMUIPackageHelper
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView
import com.theone.music.R
import com.theone.music.app.util.CacheUtil
import com.theone.music.databinding.FragmentSettingBinding
import com.theone.music.viewmodel.EventViewModel
import com.theone.mvvm.base.fragment.BaseQMUIFragment
import com.theone.mvvm.base.viewmodel.BaseViewModel
import com.theone.mvvm.core.base.fragment.BaseCoreFragment
import com.theone.mvvm.ext.getAppViewModel
import com.theone.mvvm.ext.qmui.*

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
 * @date 2022-04-07 16:19
 * @describe TODO
 * @email 625805189@qq.com
 * @remark
 */
class SettingFragment :BaseCoreFragment<BaseViewModel,FragmentSettingBinding>(),
    View.OnClickListener{

    private val mEvent: EventViewModel by lazy { getAppViewModel<EventViewModel>() }

    private lateinit var mLoginOut: QMUICommonListItemView
    private lateinit var mVersion: QMUICommonListItemView

    override fun getRootBackgroundColor(): Int = R.color.qmui_config_color_background

    override fun initView(root: View) {
        setTitleWitchBackBtn("设置")
        getDataBinding().groupListView.run {
            mVersion = createItem(
                "当前版本",
                "Ver " + QMUIPackageHelper.getAppVersion(mActivity),
                R.drawable.svg_setting_version
            )
            addToGroup(mVersion, title = "关于", listener = this@SettingFragment)

            mLoginOut = createItem("退出账号", drawable = R.drawable.svg_setting_login_out)
            if (CacheUtil.isLogin()) {
                addToGroup(mLoginOut, listener = this@SettingFragment, title = "")
            }
        }
    }

    override fun createObserver() {

    }

    override fun onClick(v: View?) {
        when (v) {
            mLoginOut -> {
                showLoginOutDialog()
            }
        }
    }

    private fun showLoginOutDialog() {
        context?.showMsgDialog(
            "提示", "是否退出当前账号",
            listener = QMUIDialogAction.ActionListener { dialog, index ->
                dialog.dismiss()
                if (index > 0) {
                    mEvent.setUserInfo(null)
                    CacheUtil.loginOut()
                    showSuccessTipsExitDialog("退出成功")
                }
            }, prop = QMUIDialogAction.ACTION_PROP_NEGATIVE
        )
    }

}