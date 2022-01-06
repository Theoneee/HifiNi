package com.theone.music.ui

import android.view.View
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.theone.common.ext.match_wrap
import com.theone.common.widget.TheSearchView
import com.theone.music.R
import com.theone.music.data.model.Music
import com.theone.music.data.model.MusicInfo
import com.theone.music.ui.adapter.MusicAdapter
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
class SearchFragment:BasePagerPullRefreshFragment<MusicInfo,MainViewModel,BasePullFreshFragmentBinding>(),TheSearchView.OnTextChangedListener {

    override fun showTopBar(): Boolean =true

    override fun initView(root: View) {
        super.initView(root)
        getTopBar()?.run {
            addLeftBackImageButton()
            addRightTextButton("搜索", R.id.topbar_right_view).setOnClickListener {
                onFirstLoading()
            }
            setCenterView(TheSearchView(requireContext(),true).apply {
                mSearchListener = this@SearchFragment
                layoutParams = RelativeLayout.LayoutParams(match_wrap).apply {
                    addRule(RelativeLayout.LEFT_OF,R.id.topbar_right_view)
                    addRule(RelativeLayout.RIGHT_OF,R.id.qmui_topbar_item_left_back)
                    marginStart = 30
                    marginEnd = 30
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

    override fun createAdapter(): BaseQuickAdapter<MusicInfo, *> = MusicAdapter()

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        val music = adapter.getItem(position) as MusicInfo
        startFragment(PlayerFragment.newInstance(music))
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