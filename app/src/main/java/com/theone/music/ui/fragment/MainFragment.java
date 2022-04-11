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

import android.content.Context;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.qmuiteam.qmui.arch.QMUIFragment;
import com.theone.music.R;
import com.theone.music.data.constant.NetConstant;
import com.theone.mvvm.base.viewmodel.BaseViewModel;
import com.theone.mvvm.core.app.widge.indicator.SkinScaleTransitionPagerTitleView;
import com.theone.mvvm.core.base.fragment.BaseTabInTitleFragment;
import com.theone.mvvm.core.data.entity.QMUITabBean;
import com.theone.mvvm.ext.qmui.QMUITopBarLayoutExtKt;

import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.util.List;
import java.util.Map;

/**
 * @author The one
 * @date 2022-04-11 11:03
 * @describe TODO
 * @email 625805189@qq.com
 * @remark
 */
public class MainFragment extends BaseTabInTitleFragment<BaseViewModel> {

    @Override
    protected void initTopBar() {
        QMUITopBarLayoutExtKt.addLeftCloseImageBtn(this, -1);
    }

    @NonNull
    @Override
    protected RelativeLayout.LayoutParams generateMagicIndicatorLayoutParams() {
        RelativeLayout.LayoutParams layoutParams = super.generateMagicIndicatorLayoutParams();
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        layoutParams.addRule(RelativeLayout.RIGHT_OF, R.id.qmui_topbar_item_left_back);
        return layoutParams;
    }

    @Nullable
    @Override
    protected IPagerIndicator getNavIndicator(@NonNull Context context) {
        return null;
    }

    @NonNull
    @Override
    protected SimplePagerTitleView getPagerTitleView(@NonNull Context context) {
        return new SkinScaleTransitionPagerTitleView(context);
    }

    @Override
    public void initTabAndFragments(@NonNull List<QMUITabBean> tabs, @NonNull List<QMUIFragment> fragments) {
        for (Map.Entry<String, Integer> entry : NetConstant.getCategoryList().entrySet()) {
            tabs.add(new QMUITabBean(entry.getKey(), -1, -1));
            fragments.add(MusicItemFragment.newInstance(entry.getValue()));
        }
    }


}
