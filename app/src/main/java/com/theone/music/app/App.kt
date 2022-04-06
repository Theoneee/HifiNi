package com.theone.music.app

import android.app.Application
import com.theone.music.BuildConfig
import com.theone.music.player.PlayerManager
import com.theone.mvvm.core.app.CoreApplication
import com.theone.mvvm.core.app.util.RxHttpManager
import com.theone.mvvm.core.base.loader.Loader
import com.theone.mvvm.core.base.loader.callback.ErrorCallback
import com.theone.mvvm.core.base.loader.callback.LoadingCallback
import com.theone.mvvm.core.base.loader.callback.SuccessCallback

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
 * @date 2022-01-04 13:13
 * @describe TODO
 * @email 625805189@qq.com
 * @remark
 */
class App:CoreApplication() {

    override fun isDebug(): Boolean = BuildConfig.DEBUG

    override fun init(application: Application) {
        PlayerManager.getInstance().init(application)
        super.init(application)
        Loader.beginBuilder()
            .addCallback(LoadingCallback::class.java)
            .addCallback(ErrorCallback::class.java)
            .defaultCallback(SuccessCallback::class.java)
            .commit()
        RxHttpManager.init().setDebug(BuildConfig.DEBUG).setOnParamAssembly {
            //添加公共请求头
            it.addHeader(
                "User-Agent",
                "Mozilla/5.0 (Linux; Android 5.0; SM-G900P Build/LRX21T) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.103 Mobile Safari/537.36"
            )
        }
    }

}