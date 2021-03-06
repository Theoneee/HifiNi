package com.theone.music.ui.fragment

import androidx.recyclerview.widget.RecyclerView
import com.theone.mvvm.core.base.fragment.BasePagerPullRefreshFragment
import com.theone.mvvm.core.base.viewmodel.BaseListViewModel
import com.theone.mvvm.core.databinding.BasePullFreshFragmentBinding
import com.theone.mvvm.core.app.widge.pullrefresh.PullRefreshLayout

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

    override fun getDataBindingClass(): Class<BasePullFreshFragmentBinding> = BasePullFreshFragmentBinding::class.java

    override fun getRecyclerView(): RecyclerView = getDataBinding().recyclerView

    override fun getRefreshLayout(): PullRefreshLayout = getDataBinding().refreshLayout

}