package com.theone.music.ui.fragment

import android.view.View
import com.theone.music.app.ext.checkLogin
import com.theone.music.data.model.User
import com.theone.music.databinding.FragmentMineBinding
import com.theone.music.viewmodel.EventViewModel
import com.theone.mvvm.core.base.fragment.BaseCoreFragment
import com.theone.music.viewmodel.MineViewModel
import com.theone.mvvm.ext.getAppViewModel

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

    private fun User.setUserInfo(){
        getViewModel().nickName.set(account)
    }

    override fun initView(root: View) {

       mAppVm.getUserInfoLiveData().value?.setUserInfo()
    }

    override fun createObserver() {
        mAppVm.getUserInfoLiveData().observe(this){
            it.setUserInfo()
        }
    }

    override fun getBindingClick(): Any = ClickProxy()

    inner class ClickProxy {

        fun login() {
            checkLogin()
        }

        fun icon() {
            checkLogin {

            }
        }

        fun setting() {

        }

    }
}