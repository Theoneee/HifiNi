package com.theone.music.ui.fragment.signer;//  ┏┓　　　┏┓
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

import android.util.SparseArray;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

import com.theone.music.BR;
import com.theone.music.R;
import com.theone.music.data.model.Singer;
import com.theone.music.databinding.FragmentSingerBinding;
import com.theone.music.ui.fragment.BaseFragment;
import com.theone.music.viewmodel.SingerViewModel;
import com.theone.mvvm.core.app.ext.LoaderExtKt;
import com.theone.mvvm.ext.qmui.QMUITopBarLayoutExtKt;

import java.util.List;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

/**
 * @author The one
 * @date 2022-04-11 14:12
 * @describe TODO
 * @email 625805189@qq.com
 * @remark
 */
public class SingerFragment extends BaseFragment<SingerViewModel, FragmentSingerBinding> {

    @Nullable
    @Override
    public View loaderRegisterView() {
        return getContentView();
    }

    @Override
    public void initView(@NonNull View view) {
        QMUITopBarLayoutExtKt.setTitleWitchBackBtn(this, "歌手");
        onPageReLoad();
    }

    @Override
    public void onPageReLoad() {
        LoaderExtKt.showLoadingPage(this, "加载中");
        getViewModel().requestServer();
    }

    @Override
    public void createObserver() {
        getViewModel().getResponseLiveData().observe(this, new Observer<List<? extends Singer>>() {
            @Override
            public void onChanged(List<? extends Singer> singers) {

            }
        });
        getViewModel().getErrorLiveData().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                LoaderExtKt.showErrorPage(SingerFragment.this,
                        s,
                        R.drawable.status_loading_view_loading_fail,
                        new Function1<View, Unit>() {
                            @Override
                            public Unit invoke(View view) {
                                onPageReLoad();
                                return null;
                            }
                        });
            }
        });
    }

    @Override
    public void applyBindingParams(@NonNull SparseArray<Object> sparseArray) {
        sparseArray.append(BR.host,this);
    }
}
