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
        // 点击登录还是注册的时候，先根据用户名查询本地的用户信息
//        val users = DataRepository.USER_DAO.getUserList(account.get())
//        // 如果是注册
//        if(isRegister.get()){
//            // users不为空说明已经注册过了
//            if(users.isNotEmpty()){
//                onError("用户名已存在")
//            }else{
//                // 这个用户名不存在，就往数据库添加一条用户数据
//                val user = User(account = account.get(),nickname = "",password = password.get().encrypt())
//                DataRepository.USER_DAO.insert(user)
//                onSuccess(user)
//            }
//        }else{
//            // 如果是登录
//            // 根据用户名查询到的用户为空
//            if(users.isEmpty()){
//                onError("用户名不存在")
//            }else{
//                // 如果查询有数据
//                val temp = users[0]
//                // 比较密码
//                if(temp.password.decrypt() != password.get()){
//                    onError("密码不正确")
//                }else{
//                    onSuccess(temp)
//                }
//            }
//        }
    }

}