package com.theone.music.viewmodel

import com.theone.mvvm.core.base.viewmodel.BaseRequestViewModel
import com.theone.common.callback.databind.BooleanObservableField
import com.theone.common.callback.databind.StringObservableField
import com.theone.music.app.ext.md5encode
import com.theone.music.data.model.User
import com.theone.music.data.repository.ApiRepository

class LoginRegisterViewModel2 : BaseRequestViewModel<User>() {

    var email = StringObservableField()
    var username = StringObservableField()
    var password = StringObservableField()
    var repassword = StringObservableField()

    var isRegister = BooleanObservableField()

    override fun requestServer() {
        request({
            if (isRegister.get()) {
                ApiRepository.INSTANCE.register(
                    email.get(),
                    username.get(),
                    password.get()
                )
            } else {
                ApiRepository.INSTANCE.login(username.get(), password.get().md5encode())
            }.await()

            val user = User().apply {
                account = username.get()
                password = this@LoginRegisterViewModel2.password.get()
                email = this@LoginRegisterViewModel2.email.get()
            }
            onSuccess(user)
        },if(isRegister.get()) "注册中" else "登录中")

    }

}