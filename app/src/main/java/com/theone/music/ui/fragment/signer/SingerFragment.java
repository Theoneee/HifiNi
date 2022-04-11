package com.theone.music.ui.fragment.signer;

// ┏┓　 ┏┓
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

import android.content.Context;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;

import com.qmuiteam.qmui.layout.QMUIFrameLayout;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.QMUIFloatLayout;
import com.theone.music.BR;
import com.theone.music.R;
import com.theone.music.app.util.ColorUtil;
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
 * @describe 歌手
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
                layoutSingers(singers);
                LoaderExtKt.showSuccessPage(SingerFragment.this);
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

    private void layoutSingers(List<? extends Singer> singers) {
        QMUIFloatLayout floatLayout = getDataBinding().floatLayout;
        Context context = getContext();
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        floatLayout.removeAllViews();

        for (Singer singer : singers) {
            QMUIFrameLayout container = new QMUIFrameLayout(context);
            int space = QMUIDisplayHelper.dp2px(context, 10);
            container.setPadding(0, 0, space, space);
            TextView tag = new TextView(context);
            int padding = QMUIDisplayHelper.dp2px(context, 4);
            int padding2 = padding * 2;
            tag.setPadding(padding2, padding, padding2, padding);
            tag.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f);
            tag.setMaxLines(1);
            tag.setTextColor(ColorUtil.INSTANCE.randomColor());
            tag.setText(singer.getName());
            tag.setBackground(ContextCompat.getDrawable(context, R.drawable.tree_tag_bg));
            container.addView(tag, layoutParams);
            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startFragment(SignerSearchFragment.newInstance(singer));
                }
            });
            floatLayout.addView(container, layoutParams);
        }
    }

    @Override
    public void applyBindingParams(@NonNull SparseArray<Object> sparseArray) {
        sparseArray.append(BR.host, this);
    }

}
