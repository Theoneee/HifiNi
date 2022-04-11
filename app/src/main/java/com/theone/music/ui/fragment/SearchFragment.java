package com.theone.music.ui.fragment;//  ┏┓　　　┏┓
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

import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;

import com.qmuiteam.qmui.util.QMUIKeyboardHelper;
import com.theone.common.widget.TheSearchView;
import com.theone.music.R;
import com.theone.music.viewmodel.SearchViewModel;
import com.theone.mvvm.core.app.ext.LoaderExtKt;
import com.theone.mvvm.ext.qmui.QMUITopBarLayoutExtKt;

/**
 * @author The one
 * @date 2022-04-11 11:40
 * @describe TODO
 * @email 625805189@qq.com
 * @remark
 */
public class SearchFragment extends BaseMusicFragment<SearchViewModel> implements TheSearchView.OnTextChangedListener{

    private TheSearchView mSearchView;

    @Override
    public void initView(@NonNull View root) {
        super.initView(root);
        mSearchView = new TheSearchView(requireContext(),true);
        mSearchView.setMSearchListener(this);
        mSearchView.getMEditText().setHint(R.string.search_hint);

        RelativeLayout.LayoutParams layoutParams  = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.LEFT_OF,R.id.topbar_right_view);
        layoutParams.addRule(RelativeLayout.RIGHT_OF, R.id.qmui_topbar_item_left_back);
        layoutParams.setMarginStart(30);
        layoutParams.setMarginEnd(30);

        mSearchView.setLayoutParams(layoutParams);
        getTopBar().setCenterView(mSearchView);
        QMUITopBarLayoutExtKt.addLeftCloseImageBtn(this,-1);
        getTopBar().addRightTextButton("搜索",R.id.topbar_right_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFirstLoading();
            }
        });

        showEmptyPage();
        QMUIKeyboardHelper.showKeyboard(mSearchView.getMEditText(),1000);

    }

    private void showEmptyPage(){
        LoaderExtKt.showEmptyPage(this,"",R.drawable.status_search_result_empty,null);
    }

    @Override
    public void onFirstLoading() {
        if(getViewModel().getKeyWord().isEmpty()){
            showEmptyPage();
            return;
        }
        super.onFirstLoading();
    }

    @Override
    public void onLoadMoreComplete() {
        onLoadMoreEnd();
        setRefreshLayoutEnabled(false);
    }

    @Override
    public void onSearchViewClick(@NonNull String s, boolean b) {
        onFirstLoading();
    }

    @Override
    public void onSearchViewTextChanged(@NonNull String s, boolean b) {
        getViewModel().setKeyWord(s);
    }


}
