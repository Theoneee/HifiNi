package com.theone.music.ui.fragment

import android.view.View
import com.theone.common.ext.notNull
import com.theone.music.app.ext.checkLogin
import com.theone.music.app.util.CacheUtil
import com.theone.music.data.model.User
import com.theone.music.databinding.FragmentMineBinding
import com.theone.music.ui.fragment.user.UserInfoFragment
import com.theone.music.viewmodel.EventViewModel
import com.theone.mvvm.core.base.fragment.BaseCoreFragment
import com.theone.music.viewmodel.MineViewModel
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
 * @date 2022-04-06 11:25
 * @describe 我的
 * @email 625805189@qq.com
 * @remark
 */
class MineFragment : BaseCoreFragment<MineViewModel, FragmentMineBinding>() {

    private val mAppVm: EventViewModel by lazy { getAppViewModel<EventViewModel>() }

    override fun isNeedChangeStatusBarMode(): Boolean = true

    override fun isStatusBarLightMode(): Boolean {
        return true
    }

    override fun showTopBar(): Boolean {
        return true
    }

    private fun User?.setUserInfo() {
        this.notNull({
            getViewModel().nickName.set(it.account)
            // 获取用户信息
            setSwipeRefreshState(true)
            getViewModel().requestServer()
        },{
            getDataBinding().swipeRefresh.isEnabled = false
            getViewModel().nickName.set("未登录")
        })
    }

    private fun setSwipeRefreshState(state:Boolean){
        getDataBinding().swipeRefresh.run{
            isRefreshing = state
            isEnabled = !state
        }
    }

    override fun initView(root: View) {
        getDataBinding().swipeRefresh.setOnRefreshListener {
            // 获取用户信息
            setSwipeRefreshState(true)
            getViewModel().requestServer()
        }
        mAppVm.getUserInfoLiveData().value.setUserInfo()
    }

    override fun createObserver() {
        getViewModel().getRequest().run {
            getResponseLiveData().observe(this@MineFragment){
                getViewModel().icon.set(it.avatar)
                CacheUtil.setUserInfo(it)
                setSwipeRefreshState(false)
            }
            getErrorLiveData().observe(this@MineFragment){
                showFailTipsDialog(it.msg)
                setSwipeRefreshState(false)
            }
        }
        mAppVm.getUserInfoLiveData().observe(this) {
            it.setUserInfo()
        }
    }

    override fun getBindingClick(): Any = ClickProxy()

    inner class ClickProxy {

        fun userInfo() {
            checkLogin {
                startFragment(UserInfoFragment())
            }
        }

        fun setting() {
            startFragment(SettingFragment())
        }

        fun collection() {
            checkLogin {
                startFragment(CollectionFragment())
            }
        }

        fun history() {
            startFragment(HistoryFragment())
        }

        fun download() {
            startFragment(DownloadFragment())
        }

    }
}