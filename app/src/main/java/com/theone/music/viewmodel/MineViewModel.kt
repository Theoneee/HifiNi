package com.theone.music.viewmodel

import com.theone.mvvm.core.base.viewmodel.BaseRequestVM
import com.theone.music.domain.request.UserInfoRequest
import com.theone.common.callback.databind.StringObservableField
import com.theone.mvvm.core.app.ext.launch

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
 * @date 2022-04-06 17:14
 * @describe TODO
 * @email 625805189@qq.com
 * @remark
 */
class MineViewModel : BaseRequestVM<UserInfoRequest>() {

    val icon = StringObservableField("https://www.hifini.com/view/img/logo.png")
    val nickName = StringObservableField("未登录")

    override fun createRequest(): UserInfoRequest {
        return UserInfoRequest()
    }

    override fun requestServer() {
        launch({
            getRequest().getUserInfo()
        })
    }

}