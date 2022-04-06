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

    fun getUserInfoLiveData():ProtectedUnPeekLiveData<User> = userInfo

    fun getCollectionLiveData(): ProtectedUnPeekLiveData<CollectionEvent> = collection

    fun getPlayMusicLiveData(): ProtectedUnPeekLiveData<Music> = playMusic

    fun getReloadMusicLiveData(): ProtectedUnPeekLiveData<Music> = reloadMusic


    fun setUserInfo(user: User){
        userInfo.value = user
        CacheUtil.setUser(user)
    }

    fun dispatchCollectionEvent(event: CollectionEvent){
        collection.value = event
    }

    fun dispatchPlayMusic(music: Music){
        playMusic.value = music
    }

    fun dispatchReloadMusic(music: Music){
        reloadMusic.value = music
    }


    init {
        userInfo.value = CacheUtil.getUser()
    }

}