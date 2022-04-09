package com.theone.music.viewmodel

import android.util.Log
import com.theone.music.data.constant.DownloadStatus
import com.theone.music.data.model.Download
import com.theone.music.data.model.DownloadResult
import com.theone.music.data.model.Music
import com.theone.music.data.repository.DataRepository
import com.theone.mvvm.core.base.viewmodel.BaseListViewModel
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
 * @date 2022-04-08 16:56
 * @describe TODO
 * @email 625805189@qq.com
 * @remark
 */
class DownloadViewModel:BaseListViewModel<DownloadResult>() {

    override fun requestServer() {
        val response = mutableListOf<DownloadResult>()
        val downloads = DataRepository.DOWNLOAD_DAO.getDownloadList(page,10)
        for (download in downloads){
            val musics = DataRepository.MUSIC_DAO.getMusicById(download.musicId)
            // 下载成功后判断是否文件存在
            if(download.status == DownloadStatus.SUCCESS){
                if(!File(download.localPath).exists()){
                    download.status = DownloadStatus.FILE_DELETE
                }
            }
            response.add(DownloadResult().apply {
                music = musics[0]
                localPath = download.localPath
                time  = download.time
                status = download.status
            })
        }
        onSuccess(response)
    }

}