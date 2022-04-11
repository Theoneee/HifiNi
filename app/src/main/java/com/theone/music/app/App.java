package com.theone.music.app;//  ┏┓　　　┏┓
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

import android.app.Application;

import androidx.annotation.NonNull;

import com.shuyu.gsyvideoplayer.cache.CacheFactory;
import com.shuyu.gsyvideoplayer.player.PlayerFactory;
import com.theone.music.BuildConfig;
import com.theone.music.player.PlayerManager;
import com.theone.mvvm.core.app.CoreApplication;
import com.theone.mvvm.core.app.util.RxHttpManager;
import com.theone.mvvm.core.base.loader.Loader;
import com.theone.mvvm.core.base.loader.callback.ErrorCallback;
import com.theone.mvvm.core.base.loader.callback.LoadingCallback;
import com.theone.mvvm.core.base.loader.callback.SuccessCallback;
import com.theone.mvvm.core.data.entity.RxHttpBuilder;

import tv.danmaku.ijk.media.exo2.Exo2PlayerManager;
import tv.danmaku.ijk.media.exo2.ExoPlayerCacheManager;

/**
 * @author The one
 * @date 2022-04-11 08:51
 * @describe TODO
 * @email 625805189@qq.com
 * @remark
 */
public class App extends CoreApplication {

    @Override
    public boolean isDebug() {
        return BuildConfig.DEBUG;
    }

    @Override
    public void init(@NonNull Application application) {
        PlayerManager.getInstance().init(application);
        super.init(application);
        // 这个是框架里面的
        Loader.Companion.beginBuilder()
                .addCallback(LoadingCallback.class)
                .addCallback(ErrorCallback.class)
                .defaultCallback(SuccessCallback.class)
                .commit();
        // 网络请求配置  RxHttp网络请求框架
        RxHttpBuilder builder = new RxHttpBuilder();
        // 配置缓存路径
        builder.setCacheFilePath(getApplicationContext().getCacheDir().getAbsolutePath());
        RxHttpManager.INSTANCE.init(builder).setOnParamAssembly(
                // 配置公共的请求
                param ->
                        // 添加请求头，模拟浏览器请求
                        param.addHeader("User-Agent",
                                "Mozilla/5.0 (Linux; Android 5.0; SM-G900P Build/LRX21T) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.103 Mobile Safari/537.36")
        );
        // 播放器配置
        //EXO模式
        PlayerFactory.setPlayManager(Exo2PlayerManager.class);
        //exo缓存模式，支持m3u8，只支持exo
        CacheFactory.setCacheManager(ExoPlayerCacheManager.class);
    }
}
