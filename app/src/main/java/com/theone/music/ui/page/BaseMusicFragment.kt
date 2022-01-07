package com.theone.music.ui.page

import android.content.Intent
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.theone.common.constant.BundleConstant
import com.theone.common.ext.startActivity
import com.theone.music.data.model.Music
import com.theone.music.ui.adapter.MusicAdapter
import com.theone.music.viewmodel.EventViewModel
import com.theone.mvvm.core.base.viewmodel.BaseListViewModel
import com.theone.mvvm.ext.getAppViewModel

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

    private val mEvent: EventViewModel by lazy { getAppViewModel<EventViewModel>() }

    override fun getViewModelIndex(): Int = 0

    override fun createAdapter(): BaseQuickAdapter<Music, *> =  MusicAdapter()

    override fun initAdapter() {
        super.initAdapter()
        (mAdapter as MusicAdapter).currentMusic = mEvent.getPlayMusicLiveData().value?.shareUrl
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        val music = adapter.getItem(position) as Music
        PlayerActivity.startPlay(mActivity,music)
    }

    override fun createObserver() {
        super.createObserver()
        mEvent.getPlayMusicLiveData().observeInFragment(this){
            (mAdapter as MusicAdapter).currentMusic = it.shareUrl
        }
    }

}