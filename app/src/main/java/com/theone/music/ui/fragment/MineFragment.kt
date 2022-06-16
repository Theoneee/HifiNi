package com.theone.music.ui.fragment

import android.view.View
import androidx.work.*
import com.theone.common.ext.notNull
import com.theone.music.app.ext.checkLogin
import com.theone.music.app.util.CacheUtil
import com.theone.music.data.model.User
import com.theone.music.databinding.FragmentMineBinding
import com.theone.music.domain.work.LoginSignWorker
import com.theone.music.ui.fragment.user.UserInfoFragment
import com.theone.music.viewmodel.EventViewModel
import com.theone.mvvm.core.base.fragment.BaseCoreFragment
import com.theone.music.viewmodel.MineViewModel
import com.theone.mvvm.ext.getAppViewModel
import java.util.concurrent.TimeUnit

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
        getViewModel().nickName.set(this?.account ?: "未登录")
    }

    override fun initView(root: View) {
        mAppVm.getUserInfoLiveData().value.setUserInfo()
    }

    override fun createObserver() {
        mAppVm.getUserInfoLiveData().observe(this) {
            it.setUserInfo()
            it?.doWorker()
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