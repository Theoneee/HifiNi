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

import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.viewpager.widget.ViewPager;

import com.qmuiteam.qmui.alpha.QMUIAlphaImageButton;
import com.qmuiteam.qmui.arch.QMUIFragment;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.util.QMUIResHelper;
import com.theone.music.R;
import com.theone.music.viewmodel.EventViewModel;
import com.theone.mvvm.base.BaseApplication;
import com.theone.mvvm.base.viewmodel.BaseViewModel;
import com.theone.mvvm.core.app.widge.indicator.SkinLinePagerIndicator;
import com.theone.mvvm.core.app.widge.indicator.SkinPagerTitleView;
import com.theone.mvvm.core.base.fragment.BaseTabInTitleFragment;
import com.theone.mvvm.core.data.entity.QMUITabBean;

import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.badge.BadgePagerTitleView;

import java.util.List;

/**
 * @author The one
 * @date 2022-04-06 11:23
 * @describe TODO
 * @email 625805189@qq.com
 * @remark
 */
public class IndexFragment extends BaseTabInTitleFragment<BaseViewModel> {
    private static final String TAG = "IndexFragment";

    protected EventViewModel mEvent;
    private QMUIAlphaImageButton mSearchBtn;

    private EventViewModel getEventVm() {
        if (null == mEvent) {
            mEvent = ((BaseApplication) mActivity.getApplication()).getAppViewModelProvider().get(EventViewModel.class);
        }
        return mEvent;
    }

    @Override
    public boolean isNeedChangeStatusBarMode() {
        return true;
    }

    @Override
    public boolean translucentFull() {
        return true;
    }

    @NonNull
    @Override
    protected RelativeLayout.LayoutParams generateMagicIndicatorLayoutParams() {
        RelativeLayout.LayoutParams layoutParams = super.generateMagicIndicatorLayoutParams();
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_START);
        layoutParams.addRule(RelativeLayout.LEFT_OF, R.id.topbar_right_view);
        layoutParams.bottomMargin = QMUIDisplayHelper.dp2px(getContext(), 10);
        return layoutParams;
    }

    @Override
    protected void initTopBar() {
        mSearchBtn = getTopBar().addRightImageButton(R.drawable.mz_titlebar_ic_search_dark, R.id.topbar_right_view);
        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startFragment(new SearchFragment());
            }
        });
        getTopBar().setBackgroundAlpha(0);
    }

    @Override
    public void initView(@NonNull View root) {
        super.initView(root);
        getViewPager().setCurrentItem(1);
        // 处理滑动的
        getViewPager().addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // 当是第二个滑动的时候才进行处理
                if (position == 1) {
                    getEventVm().dispatchPlayWidgetAlphaEvent(positionOffset);
                }
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    @Override
    public void initTabAndFragments(@NonNull List<QMUITabBean> tabs, @NonNull List<QMUIFragment> fragments) {
        tabs.add(new QMUITabBean("我的", -1, -1));
        tabs.add(new QMUITabBean("乐库", -1, -1));
        tabs.add(new QMUITabBean("MV", -1, -1));

        fragments.add(new MineFragment());
        fragments.add(new MusicRepositoryFragment());
        fragments.add(new MVFragment());
    }

    @Override
    public void createObserver() {
        super.createObserver();
        getEventVm().getPlayWidgetAlphaLiveData().observe(this, show -> {
            mSearchBtn.setImageResource(show >0.6 ? R.drawable.mz_titlebar_ic_search_light : R.drawable.mz_titlebar_ic_search_dark);
        });
    }

}
