package com.theone.music.viewmodel

import com.kunminx.architecture.ui.callback.ProtectedUnPeekLiveData
import com.kunminx.architecture.ui.callback.UnPeekLiveData
import com.theone.music.app.util.CacheUtil
import com.theone.music.data.model.CollectionEvent
import com.theone.music.data.model.Music
import com.theone.music.data.model.User
import com.theone.mvvm.base.viewmodel.BaseViewModel

class EventViewModel : BaseViewModel() {

    //App的账户信息
    private val userInfo = UnPeekLiveData.Builder<User>().setAllowNullValue(true).create()

    private val collection = UnPeekLiveData<CollectionEvent>()

    private val playMusic = UnPeekLiveData<Music>()

    private val reloadMusic = UnPeekLiveData<Music>()

    private val playWidget = UnPeekLiveData<Boolean>()

    private val playWidgetAlpha = UnPeekLiveData<Float>()

    fun getUserInfoLiveData(): ProtectedUnPeekLiveData<User> = userInfo

    fun getCollectionLiveData(): ProtectedUnPeekLiveData<CollectionEvent> = collection

    fun getPlayMusicLiveData(): ProtectedUnPeekLiveData<Music> = playMusic

    fun getReloadMusicLiveData(): ProtectedUnPeekLiveData<Music> = reloadMusic

    fun getPlayWidgetLiveData(): ProtectedUnPeekLiveData<Boolean> = playWidget

    fun getPlayWidgetAlphaLiveData(): ProtectedUnPeekLiveData<Float> = playWidgetAlpha

    fun dispatchPlayWidgetAlphaEvent(alpha: Float) {
        playWidgetAlpha.value = alpha
    }

    fun dispatchPlayWidgetEvent(show: Boolean) {
        if (show != playWidget.value) {
            playWidget.value = show
        }
    }

    fun setUserInfo(user: User?) {
        userInfo.value = user
    }

    fun dispatchCollectionEvent(event: CollectionEvent) {
        collection.value = event
    }

    /**
     * 分发当前播放的音乐的通知
     */
    fun dispatchPlayMusic(music: Music) {
        playMusic.value = music
    }

    fun dispatchReloadMusic(music: Music) {
        reloadMusic.value = music
    }


    init {
        userInfo.value = CacheUtil.getUser()
    }

}