package com.theone.music.ui

import android.view.View
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.theone.common.ext.match_wrap
import com.theone.common.widget.TheSearchView
import com.theone.music.R
import com.theone.music.data.model.Music
import com.theone.music.ui.adapter.MusicAdapter
import com.theone.music.viewmodel.MainViewModel
import com.theone.mvvm.core.base.fragment.BasePagerPullRefreshFragment
import com.theone.mvvm.core.base.viewmodel.BaseListViewModel
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
abstract class BasePagerFragment<T, VM : BaseListViewModel<T>> :BasePagerPullRefreshFragment<T,VM,BasePullFreshFragmentBinding>() {

    override fun getDataBindingClass(): Class<*> = BasePullFreshFragmentBinding::class.java

    override fun getRecyclerView(): RecyclerView = mBinding.recyclerView

    override fun getRefreshLayout(): PullRefreshLayout = mBinding.refreshLayout

    override fun onPageReLoad() {
        onFirstLoading()
    }

}