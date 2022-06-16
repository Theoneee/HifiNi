package com.theone.music.viewmodel

import android.util.Log
import com.kunminx.architecture.ui.callback.UnPeekLiveData
import com.theone.common.callback.databind.BooleanObservableField
import com.theone.common.callback.databind.StringObservableField
import com.theone.common.util.FileUtils
import com.theone.music.data.model.Lrc
import com.theone.music.data.repository.LrcRepository
import com.theone.mvvm.base.appContext
import com.theone.mvvm.core.app.ext.msg
import com.theone.mvvm.core.base.viewmodel.BaseRequestViewModel
import rxhttp.awaitResult
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
 * @date 2022-06-15 09:05
 * @describe TODO
 * @email 625805189@qq.com
 * @remark
 */
class LrcViewModel : BaseRequestViewModel<List<Lrc>>() {

    val isPlaying = BooleanObservableField()

    val musicName = StringObservableField()
    val musicSinger = StringObservableField()

    private val lrc: UnPeekLiveData<Lrc> =
        UnPeekLiveData.Builder<Lrc>().setAllowNullValue(true).create()

    private val lrcPath: UnPeekLiveData<String> =
        UnPeekLiveData.Builder<String>().setAllowNullValue(true).create()

    fun changeLrc(data:Lrc){
        lrc.value = data
    }

    fun getLrcLiveData() = lrc

    fun getLrcPathLiveData() = lrcPath

    override fun requestServer() {
        request({
            val list = LrcRepository.INSTANCE.search(musicName.get())
            onSuccess(list)
            findPrecisePLrc(list)
        })
    }

    fun isLrcExists() {
        val file = File(generateFilePath())
        if (file.exists() && file.length() > 0) {
            lrcPath.value = file.path
        }
    }

    /**
     * 找到最歌曲名称和歌手都相同的一条数据
     * @param list List<Lrc>
     */
    private fun findPrecisePLrc(list: List<Lrc>) {
        if(!lrcPath.value.isNullOrEmpty()){
            return
        }
        val singer = musicSinger.get()
        val name = musicName.get()
        // 先找歌曲名称和歌手都匹配的
        for (item in list) {
            if (name == item.name && singer == item.singer) {
                lrc.value = item
                return
            }
        }
        // 再找歌曲名称匹配的
        for (item in list) {
            if (item.name.contains(singer)) {
                lrc.value = item
                return
            }
        }
        // 再找歌曲名称匹配的
        for (item in list) {
            if (name == item.name) {
                lrc.value = item
                return
            }
        }
        if(list.isNotEmpty()){
           lrc.value = list[0]
           return
        }
        lrc.value = null
    }

    private fun generateFilePath(): String {
        val parentPath = "${appContext.cacheDir?.path}${File.separator}lrc"
        val fileName = "${musicName.get()}-${musicSinger.get()}.lrc"
        val lrcPath = parentPath + File.separator + fileName
        val parent = File(parentPath)
        if(!parent.exists()){
            parent.mkdirs()
        }
        return lrcPath
    }

    fun downloadLrc(url: String) {
        request({
            val path = generateFilePath()
            val file = File(path)
            if(!file.exists()){
                file.createNewFile()
            }
            LrcRepository.INSTANCE.download(url, path).awaitResult {
                Log.e(TAG, "downloadLrc: $it")
                lrcPath.value = it
            }.onFailure {
                Log.e(TAG, "downloadLrc error: ${it.msg}")
                File(path).delete()
                onError(it.msg)
            }
        })
    }

}