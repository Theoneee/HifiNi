package com.theone.music.viewmodel

import com.theone.music.domain.request.UserInfoRequest
import com.theone.mvvm.base.viewmodel.BaseViewModel
import com.theone.mvvm.core.app.ext.launch
import com.theone.mvvm.core.base.viewmodel.BaseRequestVM

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
 * @date 2022-06-14 10:47
 * @describe TODO
 * @email 625805189@qq.com
 * @remark
 */
class UserInfoViewModel:BaseRequestVM<UserInfoRequest>() {

    override fun createRequest(): UserInfoRequest  = UserInfoRequest()

    override fun requestServer() {
        launch({
            getRequest().getUserInfo()
        })
    }
}