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

import static com.qmuiteam.qmui.util.QMUIDisplayHelper.dp2px;

import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.theone.music.R;
import com.theone.music.data.model.Action;
import com.theone.music.data.model.Banner;
import com.theone.music.ui.adapter.BannerAdapter;
import com.theone.music.ui.adapter.MusicActionAdapter;
import com.theone.music.viewmodel.MusicRepositoryViewModel;
import com.zhpan.bannerview.BannerViewPager;
import com.zhpan.bannerview.constants.PageStyle;

import java.util.ArrayList;
import java.util.List;

/**
 * @author The one
 * @date 2022-04-06 11:40
 * @describe TODO
 * @email 625805189@qq.com
 * @remark
 */
public class MusicRepositoryFragment extends BaseMusicFragment2<MusicRepositoryViewModel> {

    private void initBannerView(BannerViewPager banner){
        List<Banner> datas = new ArrayList<>();
        datas.add(new Banner("https://y.qq.com/music/common/upload/MUSIC_FOCUS/4279661.jpg?max_age=2592000"));
        datas.add(new Banner("https://y.qq.com/music/common/upload/MUSIC_FOCUS/4280047.jpg?max_age=2592000"));
        datas.add(new Banner("https://y.qq.com/music/common/upload/MUSIC_FOCUS/4279664.jpg?max_age=2592000"));
        BannerAdapter adapter = new BannerAdapter();
        banner.setAdapter(adapter);
        banner.setLifecycleRegistry(getLifecycle());
        banner.setPageStyle(PageStyle.MULTI_PAGE_OVERLAP);
        banner.setRevealWidth(dp2px(getContext(),20));
        banner.setIndicatorVisibility(View.GONE);
        banner.setOnPageClickListener(new BannerViewPager.OnPageClickListener() {
            @Override
            public void onPageClick(View clickedView, int position) {

            }
        });
        banner.create(datas);
    }

    private void initActions(RecyclerView recyclerView){
        List<Action> actions = new ArrayList<>();
        actions.add(new Action(R.drawable.ic_action_hot,"热门"));
        actions.add(new Action(R.drawable.ic_action_rank,"排行"));
        actions.add(new Action(R.drawable.ic_action_singer,"歌手"));
        actions.add(new Action(R.drawable.ic_action_catogary,"分类"));
        MusicActionAdapter adapter  = new MusicActionAdapter();
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                Action action = (Action) adapter.getItem(position);
                if(action.res == R.drawable.ic_action_catogary){
                    startFragment(new MainFragment());
                }
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),4));
        adapter.setNewInstance(actions);
    }


    @Override
    public void initAdapter() {
        super.initAdapter();
        View topView = LayoutInflater.from(getContext()).inflate(R.layout.custom_music_repository,null,false);
        getMAdapter().addHeaderView(topView);
        BannerViewPager<Banner> bannerViewPager = topView.findViewById(R.id.banner_view);
        RecyclerView actionRc = topView.findViewById(R.id.action_recyclerView);
        initBannerView(bannerViewPager);
        initActions(actionRc);
    }

}
