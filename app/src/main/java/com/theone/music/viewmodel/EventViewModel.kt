package com.theone.music.viewmodel

import com.kunminx.architecture.ui.callback.ProtectedUnPeekLiveData
import com.kunminx.architecture.ui.callback.UnPeekLiveData
import com.theone.music.data.model.CollectionEvent
import com.theone.music.data.model.Music
import com.theone.mvvm.base.viewmodel.BaseViewModel

class EventViewModel : BaseViewModel() {

    private val collection = UnPeekLiveData<CollectionEvent>()

    fun getCollectionLiveData(): ProtectedUnPeekLiveData<CollectionEvent> = collection

    fun dispatchCollectionEvent(event: CollectionEvent){
        collection.value = event
    }

}