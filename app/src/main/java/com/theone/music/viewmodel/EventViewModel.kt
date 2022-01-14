package com.theone.music.viewmodel

import com.kunminx.architecture.ui.callback.ProtectedUnPeekLiveData
import com.kunminx.architecture.ui.callback.UnPeekLiveData
import com.theone.music.data.model.CollectionEvent
import com.theone.music.data.model.Music
import com.theone.mvvm.base.viewmodel.BaseViewModel

class EventViewModel : BaseViewModel() {

    private val collection = UnPeekLiveData<CollectionEvent>()

    private val playMusic = UnPeekLiveData<Music>()

    private val reloadMusic = UnPeekLiveData<Music>()

    fun getCollectionLiveData(): ProtectedUnPeekLiveData<CollectionEvent> = collection

    fun getPlayMusicLiveData(): ProtectedUnPeekLiveData<Music> = playMusic

    fun getReloadMusicLiveData(): ProtectedUnPeekLiveData<Music> = reloadMusic

    fun dispatchCollectionEvent(event: CollectionEvent){
        collection.value = event
    }

    fun dispatchPlayMusic(music: Music){
        playMusic.value = music
    }

    fun dispatchReloadMusic(music: Music){
        reloadMusic.value = music
    }

}