package com.theone.music.viewmodel

import com.theone.music.data.constant.DownloadStatus
import com.theone.music.data.model.DownloadResult
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
        // 下载有数据关联
        val response = mutableListOf<DownloadResult>()
        // 首先查询到所有的下载数据
        val downloads = DataRepository.DOWNLOAD_DAO.getDownloadList(page,10)
        // 然后遍历
        for (download in downloads){
            // 通过musicId得到Music信息
            val musics = DataRepository.MUSIC_DAO.getMusicById(download.musicId)
            // 下载成功后判断是否文件存在
            if(download.status == DownloadStatus.SUCCESS){
                if(!File(download.localPath).exists()){
                    download.status = DownloadStatus.FILE_DELETE
                }
            }
            // 更改格式
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