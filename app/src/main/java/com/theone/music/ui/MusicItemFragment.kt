package com.theone.music.ui

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.theone.common.constant.BundleConstant
import com.theone.common.ext.bundle
import com.theone.common.ext.getValueNonNull
import com.theone.music.data.model.Music
import com.theone.music.net.NetConstant
import com.theone.music.ui.adapter.MusicAdapter
import com.theone.music.viewmodel.MusicViewModel
import com.theone.mvvm.core.base.fragment.BasePagerPullRefreshFragment
import com.theone.mvvm.core.databinding.BasePullFreshFragmentBinding
import com.theone.mvvm.core.widge.pullrefresh.PullRefreshLayout

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
 * @date 2022-01-04 11:25
 * @describe TODO
 * @email 625805189@qq.com
 * @remark
 */
class MusicItemFragment private constructor() :
    BasePagerPullRefreshFragment<Music, MusicViewModel, BasePullFreshFragmentBinding>() {

    companion object {
        fun newInstance(type: Int): MusicItemFragment = MusicItemFragment().bundle {
            putInt(BundleConstant.TYPE, type)
        }
    }

    val mType: Int by getValueNonNull(BundleConstant.TYPE)

    override fun initData() {
        mViewModel.type = mType
        mViewModel.url = NetConstant.FORUM
    }

    override fun createAdapter(): BaseQuickAdapter<Music, *> = MusicAdapter()

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        val music = adapter.getItem(position) as Music
        startFragment(PlayerFragment.newInstance(music.link, music.name))
    }

    override fun getRecyclerView(): RecyclerView = mBinding.recyclerView

    override fun getRefreshLayout(): PullRefreshLayout = mBinding.refreshLayout


}