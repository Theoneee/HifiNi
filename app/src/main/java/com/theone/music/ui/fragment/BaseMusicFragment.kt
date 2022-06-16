package com.theone.music.ui.fragment

import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.theone.music.data.model.Music
import com.theone.music.data.model.TestAlbum
import com.theone.music.player.PlayerManager
import com.theone.music.ui.adapter.MusicAdapter
import com.theone.music.ui.activity.MusicPlayerActivity
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
abstract class BaseMusicFragment<VM : BaseListViewModel<Music>> : BasePagerFragment<Music, VM>() {


    override fun getViewModelIndex(): Int = 0

    override fun createAdapter(): BaseQuickAdapter<Music, *> = MusicAdapter()

    override fun initAdapter() {
        super.initAdapter()
        setCurrentMusic(PlayerManager.getInstance().currentPlayingMusic)
    }

    override fun onRefreshSuccess(data: List<Music>) {
        getAdapter().getDiffer().submitList(data.toMutableList()) {
            setRefreshLayoutEnabled(true)
            getRecyclerView().scrollToPosition(0)
        }
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        val music = adapter.getItem(position) as Music
        MusicPlayerActivity.startPlay(mActivity, music)
    }

    override fun createObserver() {
        super.createObserver()
        PlayerManager.getInstance().changeMusicEvent.observe(this){
            setCurrentMusic(it.music as TestAlbum.TestMusic)
        }
    }

    private fun setCurrentMusic(music: TestAlbum.TestMusic?){
        music?.let {
            (getAdapter() as MusicAdapter).currentMusic = it.shareUrl
        }
    }

}