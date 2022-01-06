package com.theone.music.ui

import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.theone.music.data.model.MusicInfo
import com.theone.music.ui.adapter.CollectionAdapter
import com.theone.music.viewmodel.CollectionViewModel

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
 * @date 2022-01-06 13:41
 * @describe TODO
 * @email 625805189@qq.com
 * @remark
 */
class CollectionFragment:BasePagerFragment<MusicInfo,CollectionViewModel>() {

    override fun createAdapter(): BaseQuickAdapter<MusicInfo, *> =  CollectionAdapter()

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        val music = adapter.getItem(position) as MusicInfo
        startFragment(PlayerFragment.newInstance(music))
    }

    override fun onLoadMoreComplete() {
        onLoadMoreEnd()
    }

}