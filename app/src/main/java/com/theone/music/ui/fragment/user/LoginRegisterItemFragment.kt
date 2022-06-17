package com.theone.music.ui.fragment.user

import android.view.View
import androidx.lifecycle.Observer
import androidx.work.*
import com.theone.common.constant.BundleConstant.TYPE
import com.theone.common.ext.bundle
import com.theone.common.ext.getColor
import com.theone.common.ext.getValueNonNull
import com.theone.music.R
import com.theone.music.app.util.CacheUtil
import com.theone.music.data.model.User
import com.theone.music.databinding.FragmentLoginRegister2Binding
import com.theone.music.domain.work.LoginSignWorker
import com.theone.music.viewmodel.EventViewModel
import com.theone.music.viewmodel.LoginRegisterViewModel2
import com.theone.mvvm.ext.getAppViewModel
import com.theone.mvvm.core.base.fragment.BaseCoreFragment
import com.theone.mvvm.ext.qmui.showFailTipsDialog
import com.theone.mvvm.ext.qmui.showSuccessTipsExitDialog
import java.util.concurrent.TimeUnit

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
            getResponseLiveData().observe(this@LoginRegisterItemFragment) {
                mAppVm.setUserInfo(it)
                CacheUtil.setUser(it)
                it.doWorker()
                showSuccessTipsExitDialog(if (isRegister.get()) "注册成功" else "登录成功")
            }
            getErrorLiveData().observe(this@LoginRegisterItemFragment) {
                showFailTipsDialog(it)
            }
        }
    }

    /**
     * 开启每日登录签到任务
     * @receiver User
     */
    private fun User.doWorker() {
        // 添加约束，网络连接时才启用
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)  // 网络状态
            .setRequiresBatteryNotLow(false)                 // 不在电量不足时执行
            .setRequiresCharging(false)                      // 在充电时执行
            .setRequiresStorageNotLow(false)                 // 不在存储容量不足时执行
            //.setRequiresDeviceIdle(true)                    // 在待机状态下执行，需要 API 23
            .build()

        val inputData = Data.Builder().putString(LoginSignWorker.EMAIL, account)
            .putString(LoginSignWorker.PASSWORD, password).build()

        // 周期任务  一天执行一次签到
        val signRequest = PeriodicWorkRequest
            .Builder(LoginSignWorker::class.java, 1, TimeUnit.DAYS)
            .setConstraints(constraints)
            .setInputData(inputData)
            .addTag(LoginSignWorker.TAG)
            .build()

        WorkManager.getInstance(mActivity).enqueue(signRequest)
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