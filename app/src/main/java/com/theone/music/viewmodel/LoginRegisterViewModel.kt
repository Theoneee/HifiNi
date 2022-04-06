package com.theone.music.viewmodel

import com.theone.mvvm.core.base.viewmodel.BaseRequestViewModel
import com.theone.common.callback.databind.BooleanObservableField
import com.theone.common.callback.databind.StringObservableField
import com.theone.music.app.ext.decrypt
import com.theone.music.app.ext.encrypt
import com.theone.music.data.model.User
import com.theone.music.data.repository.DataRepository

class LoginRegisterViewModel : BaseRequestViewModel<User>() {

    var account = StringObservableField()
    var password = StringObservableField()
    var repassword = StringObservableField()

    var isRegister = BooleanObservableField()

    override fun requestServer() {
        val users = DataRepository.USER_DAO.getUserList(account.get())
        if(isRegister.get()){
            if(users.isNotEmpty()){
                onError("用户名已存在")
            }else{
                val user = User(account = account.get(),nickname = "",password = password.get().encrypt())
                DataRepository.USER_DAO.insert(user)
                onSuccess(user)
            }
        }else{
            if(users.isEmpty()){
                onError("用户名不存在")
            }else{
                val temp = users[0]
                if(temp.password.decrypt() != password.get()){
                    onError("密码不正确")
                }else{
                    onSuccess(temp)
                }
            }
        }
    }

}