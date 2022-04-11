package com.theone.music.ui.fragment.user;//  ┏┓　　　┏┓
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.qmuiteam.qmui.arch.QMUIFragment;
import com.theone.music.R;
import com.theone.mvvm.base.viewmodel.BaseViewModel;
import com.theone.mvvm.core.app.widge.indicator.SkinScaleTransitionPagerTitleView;
import com.theone.mvvm.core.base.fragment.BaseTabInTitleFragment;
import com.theone.mvvm.core.data.entity.QMUITabBean;
import com.theone.mvvm.ext.qmui.QMUITopBarLayoutExtKt;

import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.util.List;

/**
 * @author The one
 * @date 2022-04-11 14:46
 * @describe TODO
 * @email 625805189@qq.com
 * @remark
 */
public class LoginRegisterFragment extends BaseTabInTitleFragment<BaseViewModel> {

    @Override
    protected void initTopBar() {
        QMUITopBarLayoutExtKt.addLeftCloseImageBtn(this, R.drawable.mz_comment_titlebar_ic_close_dark);
        getTopBar().updateBottomDivider(0,0,0,0);
    }

    @Override
    public TransitionConfig onFetchTransitionConfig() {
        return SCALE_TRANSITION_CONFIG;
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
        tabs.add(new QMUITabBean("登录",-1,-1));
        tabs.add(new QMUITabBean("注册",-1,-1));

        fragments.add(LoginRegisterItemFragment.newInstance(false));
        fragments.add(LoginRegisterItemFragment.newInstance(true));
    }



}
