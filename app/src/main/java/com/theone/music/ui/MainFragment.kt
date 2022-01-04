package com.theone.music.ui

import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.theone.common.ext.match_wrap
import com.theone.common.widget.TheSearchView
import com.theone.music.R
import com.theone.music.app.ext.writeStringToFile
import com.theone.music.data.model.Music
import com.theone.music.databinding.PageMainBinding
import com.theone.music.ui.adapter.SearchAdapter
import com.theone.music.viewmodel.MainViewModel
import com.theone.mvvm.core.base.fragment.BaseCoreFragment
import com.theone.mvvm.core.base.fragment.BasePagerPullRefreshFragment
import com.theone.mvvm.core.databinding.BasePullFreshFragmentBinding
import com.theone.mvvm.core.databinding.BaseRecyclerPagerFragmentBinding
import com.theone.mvvm.core.ext.addFailTipsObserve
import com.theone.mvvm.core.util.FileDirectoryUtil
import com.theone.mvvm.core.widge.pullrefresh.PullRefreshLayout
import com.theone.mvvm.ext.qmui.setTitleWitchBackBtn
import java.io.File

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
class MainFragment:BasePagerPullRefreshFragment<Music,MainViewModel,BasePullFreshFragmentBinding>(),TheSearchView.OnTextChangedListener {

    override fun initView(root: View) {
        super.initView(root)
        getTopBar()?.run {
            addRightTextButton("搜索", R.id.topbar_right_view).setOnClickListener {
                onFirstLoading()
            }
            setCenterView(TheSearchView(requireContext(),true).apply {
                mSearchListener = this@MainFragment
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
        super.onFirstLoading()
    }

    override fun createAdapter(): BaseQuickAdapter<Music, *> = SearchAdapter()

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        val music = adapter.getItem(position) as Music
        startFragment(MusicInfoFragment.newInstance(music.link))
    }

    override fun onLoadMoreComplete() {
        onLoadMoreEnd()
    }

    override fun getRecyclerView(): RecyclerView = mBinding.recyclerView

    override fun getRefreshLayout(): PullRefreshLayout = mBinding.refreshLayout

    override fun onSearchViewClick(content: String, empty: Boolean) {
        Log.e(TAG, "onSearchViewClick: " )
//        onFirstLoading()
    }

    override fun onSearchViewTextChanged(content: String, empty: Boolean) {
        mViewModel.keyWord = content
    }

}