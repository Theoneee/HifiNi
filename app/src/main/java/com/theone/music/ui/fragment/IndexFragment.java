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
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;

import com.qmuiteam.qmui.arch.QMUIFragment;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.theone.music.R;
import com.theone.mvvm.base.viewmodel.BaseViewModel;
import com.theone.mvvm.core.base.fragment.BaseTabInTitleFragment;
import com.theone.mvvm.core.data.entity.QMUITabBean;

import java.util.List;

/**
 * @author The one
 * @date 2022-04-06 11:23
 * @describe TODO
 * @email 625805189@qq.com
 * @remark
 */
public class IndexFragment extends BaseTabInTitleFragment<BaseViewModel> {

    @NonNull
    @Override
    protected RelativeLayout.LayoutParams generateMagicIndicatorLayoutParams() {
        RelativeLayout.LayoutParams layoutParams = super.generateMagicIndicatorLayoutParams();
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_START);
        layoutParams.addRule(RelativeLayout.LEFT_OF, R.id.topbar_right_view);
        layoutParams.bottomMargin = QMUIDisplayHelper.dp2px(getContext(),10);
        return layoutParams;
    }

    @Override
    protected void initTopBar() {
        getTopBar().addRightImageButton(R.drawable.mz_titlebar_ic_search_dark, R.id.topbar_right_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startFragment(new SearchFragment());
            }
        });
    }

    @Override
    public void initTabAndFragments(@NonNull List<QMUITabBean> tabs, @NonNull List<QMUIFragment> fragments) {
        tabs.add(new QMUITabBean("我的", -1, -1));
        tabs.add(new QMUITabBean("乐库", -1, -1));

        fragments.add(new MineFragment());
        fragments.add(new MusicRepositoryFragment());
    }

}
