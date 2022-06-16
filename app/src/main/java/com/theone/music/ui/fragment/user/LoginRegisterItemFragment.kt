package com.theone.music.ui.fragment.user

import android.view.View
import androidx.lifecycle.Observer
import com.theone.common.constant.BundleConstant.TYPE
import com.theone.common.ext.bundle
import com.theone.common.ext.getColor
import com.theone.common.ext.getValueNonNull
import com.theone.music.R
import com.theone.music.app.util.CacheUtil
import com.theone.music.databinding.FragmentLoginRegister2Binding
import com.theone.music.viewmodel.EventViewModel
import com.theone.music.viewmodel.LoginRegisterViewModel2
import com.theone.mvvm.ext.getAppViewModel
import com.theone.mvvm.core.base.fragment.BaseCoreFragment
import com.theone.mvvm.ext.qmui.showFailTipsDialog
import com.theone.mvvm.ext.qmui.showSuccessTipsExitDialog

class LoginRegisterItemFragment private constructor() :
    BaseCoreFragment<LoginRegisterViewModel2, FragmentLoginRegister2Binding>() {

    companion object {
        fun newInstant(isRegister: Boolean): LoginRegisterItemFragment {
            return LoginRegisterItemFragment().bundle {
                putBoolean(TYPE, isRegister)
            }
        }
    }

    private val mAppVm: EventViewModel by lazy { getAppViewModel<EventViewModel>() }

    private val isRegister: Boolean by getValueNonNull(TYPE)

    override fun getBindingClick(): Any = ProxyClick()

    override fun initView(root: View) {
        root.setBackgroundColor(getColor(mActivity, R.color.white))
        getViewModel().isRegister.set(isRegister)
    }

    override fun createObserver() {
        getViewModel().run {
            getResponseLiveData().observe(this@LoginRegisterItemFragment, Observer {
                mAppVm.setUserInfo(it)
                CacheUtil.setUser(it)
                showSuccessTipsExitDialog(if (isRegister.get()) "注册成功" else "登录成功")
            })
            getErrorLiveData().observe(this@LoginRegisterItemFragment, Observer {
                showFailTipsDialog(it)
            })
        }
    }

    inner class ProxyClick {

        fun login() {
            with(getViewModel()) {
                when {
                    isRegister.get() && email.get().isEmpty() -> showFailTipsDialog("请填写邮箱")
                    username.get().isEmpty() -> showFailTipsDialog("请填写账号")
                    password.get().isEmpty() -> showFailTipsDialog("请填写密码")
                    isRegister.get() && repassword.get().isEmpty() -> showFailTipsDialog("请填写确认密码")
                    else -> {
                        requestServer()
                    }
                }
            }
        }
    }

}