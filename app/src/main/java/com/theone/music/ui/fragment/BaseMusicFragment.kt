package com.theone.music.ui.fragment

import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.theone.common.ext.getColor
import com.theone.music.R
import com.theone.music.app.ext.removeItem
import com.theone.music.data.model.Music
import com.theone.music.ui.activity.PlayerActivity
import com.theone.music.ui.adapter.MusicAdapter
import com.theone.music.viewmodel.EventViewModel
import com.theone.mvvm.core.app.widge.loadsir.callback.SuccessCallback
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
abstract class BaseMusicFragment<VM : BaseListViewModel<Music>> : BasePagerFragment<Music, VM>() {

    private val mEvent: EventViewModel by lazy { getAppViewModel<EventViewModel>() }

    override fun getViewModelIndex(): Int = 0

    override fun createAdapter(): BaseQuickAdapter<Music, *> = MusicAdapter()

    override fun initView(root: View) {
        root.setBackgroundColor(getColor(mActivity, R.color.white))
        super.initView(root)
    }

    override fun initAdapter() {
        super.initAdapter()
        (mAdapter as MusicAdapter).currentMusic = mEvent.getPlayMusicLiveData().value?.shareUrl
    }

    override fun onRefreshSuccess(data: List<Music>) {
        mAdapter.getDiffer().submitList(data.toMutableList()) {
            setRefreshLayoutEnabled(true)
            getRecyclerView().scrollToPosition(0)
        }
    }

    override fun onAutoRefresh() {
        if (getLoadSir()?.currentCallback is SuccessCallback) {
            onRefresh()
        } else {
            onFirstLoading()
        }
    }

    override fun onFirstLoading() {
        mViewModel.isFirst = true
        super.onFirstLoading()
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        val music = adapter.getItem(position) as Music
        PlayerActivity.startPlay(mActivity, music)
    }

    override fun createObserver() {
        super.createObserver()
        mEvent.getPlayMusicLiveData().observeInFragment(this) {
            (mAdapter as MusicAdapter).currentMusic = it.shareUrl
        }

    }

}