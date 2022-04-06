package com.theone.music.viewmodel

import com.kunminx.architecture.ui.callback.UnPeekLiveData
import com.theone.music.app.util.CacheUtil
import com.theone.music.data.model.User
import com.theone.mvvm.base.viewmodel.BaseViewModel

class AppViewModel: BaseViewModel() {

    //App的账户信息
    var userInfo = UnPeekLiveData.Builder<User>().setAllowNullValue(true).create()

    init {
        userInfo.value = CacheUtil.getUser()
    }

}