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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.theone.mvvm.core.app.widge.pullrefresh.PullRefreshLayout;
import com.theone.mvvm.core.base.fragment.BasePagerPullRefreshFragment;
import com.theone.mvvm.core.base.viewmodel.BaseListViewModel;
import com.theone.mvvm.core.databinding.BasePullFreshFragmentBinding;
import com.theone.mvvm.ext.qmui.QMUITopBarLayoutExtKt;

/**
 * @author The one
 * @date 2022-04-11 10:37
 * @describe TODO
 * @email 625805189@qq.com
 * @remark
 */
abstract class BasePagerFragment<T, VM extends BaseListViewModel<T>> extends BasePagerPullRefreshFragment<T, VM, BasePullFreshFragmentBinding> {

    protected String getTopBarTitle() {
        return null;
    }

    @Override
    public void initView(@NonNull View root) {
        String title = getTopBarTitle();
        if (null != title) {
            QMUITopBarLayoutExtKt.setTitleWitchBackBtn(this, title);
        }
        super.initView(root);
    }

    @NonNull
    @Override
    public Class<BasePullFreshFragmentBinding> getDataBindingClass() {
        return BasePullFreshFragmentBinding.class;
    }

    @NonNull
    @Override
    public RecyclerView getRecyclerView() {
        return getDataBinding().recyclerView;
    }

    @Nullable
    @Override
    public PullRefreshLayout getRefreshLayout() {
        return getDataBinding().refreshLayout;
    }

}
