package com.theone.music.ui.fragment

import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.theone.music.data.model.Download
import com.theone.music.data.model.DownloadResult
import com.theone.music.ui.adapter.DownloadAdapter
import com.theone.music.viewmodel.DownloadViewModel
import com.theone.mvvm.ext.qmui.setTitleWitchBackBtn

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
 * @describe 我的下载
 * @email 625805189@qq.com
 * @remark
 */
class DownloadFragment:BasePagerFragment<DownloadResult, DownloadViewModel>() {

    override fun initView(root: View) {
        super.initView(root)
        setTitleWitchBackBtn("我的下载")
    }

    override fun createAdapter(): BaseQuickAdapter<DownloadResult, *>  = DownloadAdapter()

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {

    }

}