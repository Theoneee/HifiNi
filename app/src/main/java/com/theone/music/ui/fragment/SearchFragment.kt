package com.theone.music.ui.fragment

import android.view.View
import android.widget.RelativeLayout
import com.qmuiteam.qmui.util.QMUIKeyboardHelper
import com.theone.common.ext.match_wrap
import com.theone.common.widget.TheSearchView
import com.theone.music.R
import com.theone.music.viewmodel.MainViewModel
import com.theone.mvvm.core.app.ext.showEmptyPage

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
class SearchFragment: BaseMusicFragment<MainViewModel>(),TheSearchView.OnTextChangedListener {

    private val mSearchView:TheSearchView by lazy {
        TheSearchView(requireContext(),true).apply {
            mSearchListener = this@SearchFragment
            layoutParams = RelativeLayout.LayoutParams(match_wrap).apply {
                addRule(RelativeLayout.LEFT_OF,R.id.topbar_right_view)
                addRule(RelativeLayout.RIGHT_OF,R.id.qmui_topbar_item_left_back)
                marginStart = 30
                marginEnd = 30
            }
            mEditText.setHint(R.string.search_hint)
        }
    }

    override fun showTopBar(): Boolean =true

    override fun initView(root: View) {
        super.initView(root)
        getTopBar()?.run {
            addLeftBackImageButton()
            addRightTextButton("搜索", R.id.topbar_right_view).setOnClickListener {
                onFirstLoading()
            }
            setCenterView(mSearchView)
        }
        QMUIKeyboardHelper.showKeyboard(mSearchView.mEditText,200)
    }

    override fun onFirstLoading() {
        if(getViewModel().keyWord.isEmpty()){
            showEmptyPage("")
            return
        }
        getViewModel().isFirst = true
        getViewModel().isFresh = false
        super.onFirstLoading()
    }

    override fun onLoadMoreComplete() {
        onLoadMoreEnd()
        setRefreshLayoutEnabled(false)
    }

    override fun onSearchViewClick(content: String, empty: Boolean) {
        onFirstLoading()
    }

    override fun onSearchViewTextChanged(content: String, empty: Boolean) {
        getViewModel().keyWord = content
    }

}