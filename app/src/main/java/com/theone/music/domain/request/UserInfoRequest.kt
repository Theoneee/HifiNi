package com.theone.music.domain.request

import com.kunminx.architecture.ui.callback.UnPeekLiveData
import com.theone.music.data.model.UserInfo
import com.theone.music.data.repository.ApiRepository
import com.theone.music.data.repository.JsoupRepository
import com.theone.mvvm.core.app.ext.code
import com.theone.mvvm.core.app.ext.msg
import com.theone.mvvm.core.base.request.BaseRequest
import com.theone.mvvm.core.data.entity.ErrorInfo
import rxhttp.awaitResult
import rxhttp.wrapper.coroutines.Await

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
class UserInfoRequest : BaseRequest<UserInfo>() {

    suspend fun getUserInfo() {
        ApiRepository.INSTANCE.userInfo().awaitResult { result ->
            onSuccess(JsoupRepository.INSTANCE.parseUserInfo(result))
        }.onFailure {
            // 错误回调
            onError(ErrorInfo(it.msg, it.code))
        }
    }

}