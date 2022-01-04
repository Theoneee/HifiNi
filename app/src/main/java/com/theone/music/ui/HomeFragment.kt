package com.theone.music.ui

import android.view.View
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.theone.common.ext.match_wrap
import com.theone.common.widget.TheSearchView
import com.theone.music.R
import com.theone.music.data.model.Music
import com.theone.music.ui.adapter.SearchAdapter
import com.theone.music.viewmodel.MainViewModel
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
class HomeFragment:BasePagerPullRefreshFragment<Music,MainViewModel,BasePullFreshFragmentBinding>(),TheSearchView.OnTextChangedListener {

    override fun showTopBar(): Boolean =true

    override fun initView(root: View) {
        super.initView(root)
        getTopBar()?.run {
            addRightTextButton("搜索", R.id.topbar_right_view).setOnClickListener {
                onFirstLoading()
            }
            setCenterView(TheSearchView(requireContext(),true).apply {
                mSearchListener = this@HomeFragment
                layoutParams = RelativeLayout.LayoutParams(match_wrap).apply {
                    addRule(RelativeLayout.LEFT_OF,R.id.topbar_right_view)
                    marginStart = 50
                }
            })
        }
    }

    override fun onFirstLoading() {
        if(mViewModel.keyWord.isEmpty()){
            return
        }
        mViewModel.isFirst = true
        mViewModel.isFresh = false
        super.onFirstLoading()
    }

    override fun createAdapter(): BaseQuickAdapter<Music, *> = SearchAdapter()

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        val music = adapter.getItem(position) as Music
        startFragment(MusicInfoFragment.newInstance(music.link,music.name))
    }

    override fun onLoadMoreComplete() {
        onLoadMoreEnd()
    }

    override fun getRecyclerView(): RecyclerView = mBinding.recyclerView

    override fun getRefreshLayout(): PullRefreshLayout = mBinding.refreshLayout

    override fun onSearchViewClick(content: String, empty: Boolean) {
        onFirstLoading()
    }

    override fun onSearchViewTextChanged(content: String, empty: Boolean) {
        mViewModel.keyWord = content
    }

}