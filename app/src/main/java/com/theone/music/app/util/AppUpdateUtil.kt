package com.theone.music.app.util

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.theone.music.data.model.Update
import com.theone.music.data.repository.ApiRepository
import com.theone.mvvm.core.app.util.BaseAppUpdateUtil
import kotlinx.coroutines.launch

class AppUpdateUtil(val activity: AppCompatActivity, showCheck: Boolean) :
    BaseAppUpdateUtil<Update>(activity, showCheck) {

    override fun requestServe() {
        activity.lifecycleScope.launch {
            val result = ApiRepository.INSTANCE.checkUpdate().await()
            result.onComplete()
        }
    }


}