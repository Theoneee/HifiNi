package com.theone.music.app

import android.app.Application
import com.theone.music.BuildConfig
import com.theone.mvvm.core.CoreApplication
import com.theone.mvvm.core.util.RxHttpManager
import rxhttp.RxHttpPlugins

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
        super.init(application)
        RxHttpManager.init().setDebug(BuildConfig.DEBUG).setOnParamAssembly {
            //添加公共请求头
            it.addHeader("User-Agent", "Mozilla/5.0 (Linux; Android 5.0; SM-G900P Build/LRX21T) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.103 Mobile Safari/537.36")
        }
    }

}