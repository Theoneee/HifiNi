package com.theone.music.ui.page

import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.theone.music.data.model.Music
import com.theone.music.ui.adapter.MusicAdapter
import com.theone.mvvm.core.base.viewmodel.BaseListViewModel

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
abstract class BaseMusicFragment<VM:BaseListViewModel<Music>>: BasePagerFragment<Music, VM>() {

    override fun getViewModelIndex(): Int = 0

    override fun createAdapter(): BaseQuickAdapter<Music, *> =  MusicAdapter()

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        val music = adapter.getItem(position) as Music
        startFragment(PlayerFragment.newInstance(music))
    }

}