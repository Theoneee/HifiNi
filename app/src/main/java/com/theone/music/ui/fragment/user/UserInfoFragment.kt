package com.theone.music.ui.fragment.user

import android.view.View
import com.qmuiteam.qmui.widget.QMUITopBarLayout
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView
import com.theone.loader.callback.Callback
import com.theone.music.R
import com.theone.music.data.model.UserInfo
import com.theone.music.data.repository.JsoupRepository
import com.theone.music.databinding.FragmentUserInfoBinding
import com.theone.music.viewmodel.UserInfoViewModel
import com.theone.mvvm.core.app.ext.showSuccessPage
import com.theone.mvvm.core.base.fragment.BaseCoreFragment
import com.theone.mvvm.core.base.loader.callback.LoadingCallback
import com.theone.mvvm.ext.qmui.addToGroup
import com.theone.mvvm.ext.qmui.createItem
import com.theone.mvvm.ext.qmui.setTitleWithBackBtn

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
 * @date 2022-06-14 10:52
 * @describe TODO
 * @email 625805189@qq.com
 * @remark
 */
class UserInfoFragment:BaseCoreFragment<UserInfoViewModel,FragmentUserInfoBinding>() {

    private lateinit var mTheme:QMUICommonListItemView
    private lateinit var mPost:QMUICommonListItemView
    private lateinit var mCollection:QMUICommonListItemView
    private lateinit var mCoin:QMUICommonListItemView
    private lateinit var mGroup:QMUICommonListItemView
    private lateinit var mCreateDate:QMUICommonListItemView
    private lateinit var mLastLoginDate:QMUICommonListItemView
    private lateinit var mEmail:QMUICommonListItemView

    override fun loaderRegisterView(): View = getContentView()

    override fun loaderDefaultCallback(): Class<out Callback> = LoadingCallback::class.java

    override fun getRootBackgroundColor(): Int = R.color.qmui_config_color_background

    override fun QMUITopBarLayout.initTopBar() {
        setTitleWithBackBtn("用户信息",this@UserInfoFragment)
    }

    override fun initView(root: View) {
        getDataBinding().groupListView.run {
            mTheme = createItem("主题")
            mPost = createItem("帖子")
            mCollection = createItem("收藏")
            mCoin = createItem("金币")
            mGroup = createItem("用户组")
            mCreateDate = createItem("创建时间")
            mLastLoginDate = createItem("最后登录")
            mEmail = createItem("Email")

            addToGroup(mTheme,mPost,mCollection,mCoin, title = "")
            addToGroup(mEmail,mGroup,mCreateDate,mLastLoginDate, title = "")
        }

        getViewModel().requestServer()
    }

    override fun createObserver() {
        getViewModel().getRequest().run {
            getResponseLiveData().observe(this@UserInfoFragment){
                it.setUserInfo()
                showSuccessPage()
            }
            getErrorLiveData().observe(this@UserInfoFragment){

            }
        }
    }

    private fun UserInfo.setUserInfo(){
        mTheme.detailText = theme
        mPost.detailText = post
        mCollection.detailText = collection
        mCoin.detailText = coin
        mEmail.detailText = email
        mGroup.detailText = group
        mCreateDate.detailText = createData
        mLastLoginDate.detailText = lastLoginDate
    }


}