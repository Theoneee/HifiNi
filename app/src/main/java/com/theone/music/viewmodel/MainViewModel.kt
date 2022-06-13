package com.theone.music.viewmodel

import com.theone.common.callback.databind.StringObservableField
import com.theone.music.app.ext.md5encode
import com.theone.music.data.repository.ApiRepository
import com.theone.mvvm.core.app.ext.launch
import com.theone.mvvm.core.base.viewmodel.BaseRequestViewModel

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
 * @date 2022-06-13 15:23
 * @describe TODO
 * @email 625805189@qq.com
 * @remark
 */
class MainViewModel : BaseRequestViewModel<String>() {

    val email = StringObservableField("625805189@qq.com")
    val password = StringObservableField("ss5523..")

    override fun requestServer() {
        launch({
            ApiRepository.INSTANCE.login(email.get(), password.get().md5encode()).await()
            val result = ApiRepository.INSTANCE.sign().await()
            onSuccess("签到成功")
        })
    }

}