package com.theone.music.ui.page

import android.util.Log
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.theone.music.data.model.Music
import com.theone.music.player.PlayerManager
import com.theone.music.ui.adapter.MusicAdapter
import com.theone.music.viewmodel.EventViewModel
import com.theone.mvvm.core.base.viewmodel.BaseListViewModel
import com.theone.mvvm.ext.getAppViewModel
import javax.security.auth.login.LoginException
import kotlin.math.log

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
        startFragment(PlayerFragment.newInstance(music))
    }

    override fun createObserver() {
        super.createObserver()
        mEvent.getPlayMusicLiveData().observeInFragment(this){
            (mAdapter as MusicAdapter).currentMusic = it.shareUrl
        }

    }

}