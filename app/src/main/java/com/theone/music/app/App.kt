package com.theone.music.app

import android.app.Application
import com.shuyu.gsyvideoplayer.cache.CacheFactory
import com.shuyu.gsyvideoplayer.player.PlayerFactory
import com.theone.music.BuildConfig
import com.theone.music.player.PlayerManager
import com.theone.mvvm.core.app.CoreApplication
import com.theone.mvvm.core.app.util.RxHttpManager
import com.theone.mvvm.core.base.loader.Loader
import com.theone.mvvm.core.base.loader.callback.ErrorCallback
import com.theone.mvvm.core.base.loader.callback.LoadingCallback
import com.theone.mvvm.core.base.loader.callback.SuccessCallback
import com.theone.mvvm.core.data.entity.RxHttpBuilder
import tv.danmaku.ijk.media.exo2.Exo2PlayerManager
import tv.danmaku.ijk.media.exo2.ExoPlayerCacheManager
import java.io.File

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
        // 对播放器的初始化
        PlayerManager.getInstance().init(application)
        super.init(application)
        // 这个是框架里面的
        Loader.beginBuilder()
            .addCallback(LoadingCallback::class.java)
            .addCallback(ErrorCallback::class.java)
            .defaultCallback(SuccessCallback::class.java)
            .commit()
        // 网络请求
        RxHttpManager.init(RxHttpBuilder().apply {
            // 这个是缓存地址  app.cacheDir.absolutePath 这个地址是APP内部的，这个是不需要请求权限的
            cacheFilePath = app.cacheDir.absolutePath
        }).setDebug(BuildConfig.DEBUG).setOnParamAssembly {
            //添加公共请求头
            it.addHeader(
                "User-Agent",
                "Mozilla/5.0 (Linux; Android 5.0; SM-G900P Build/LRX21T) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.103 Mobile Safari/537.36"
            )
        }
        // 这个是播放器的配置
        PlayerFactory.setPlayManager(Exo2PlayerManager::class.java) //EXO模式
        CacheFactory.setCacheManager(ExoPlayerCacheManager::class.java) //exo缓存模式，支持m3u8，只支持exo
    }

}