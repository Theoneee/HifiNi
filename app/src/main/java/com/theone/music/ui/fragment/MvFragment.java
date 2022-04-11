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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoView;
import com.theone.music.R;
import com.theone.music.data.model.MV;
import com.theone.music.player.PlayerManager;
import com.theone.music.ui.adapter.MvAdapter;
import com.theone.music.ui.view.LoverVideoPlayer;
import com.theone.music.ui.view.OnViewPagerListener;
import com.theone.music.ui.view.PagerLayoutManager;
import com.theone.music.viewmodel.EventViewModel;
import com.theone.music.viewmodel.MvViewModel;
import com.theone.mvvm.base.BaseApplication;

/**
 * @author The one
 * @date 2022-04-11 11:21
 * @describe TODO
 * @email 625805189@qq.com
 * @remark
 */
public class MvFragment extends BasePagerFragment<MV, MvViewModel> {

    private int mPlayPosition = 0;
    private boolean isPageVisible = false;

    private EventViewModel mEvent;

    protected EventViewModel getEventVm() {
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
    public boolean isStatusBarLightMode() {
        return false;
    }

    @Override
    public boolean translucentFull() {
        return true;
    }

    @Override
    public boolean isLazyLoadData() {
        return false;
    }

    @Override
    public void setRefreshLayoutEnabled(boolean enabled) {
        super.setRefreshLayoutEnabled(false);
    }

    @NonNull
    @Override
    public BaseQuickAdapter<MV, ?> createAdapter() {
        return new MvAdapter();
    }

    @Override
    public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {

    }

    @NonNull
    @Override
    public RecyclerView.LayoutManager getLayoutManager() {
        PagerLayoutManager layoutManager = new PagerLayoutManager(getContext(), LinearLayoutManager.VERTICAL);
        layoutManager.setOnViewPagerListener(new OnViewPagerListener() {
            @Override
            public void onInitComplete(View view) {
                startPlayVideo(mPlayPosition,view);
            }

            @Override
            public void onPageRelease(boolean isNext, int position, View view) {
                LoverVideoPlayer videoPlayer = view.findViewById(R.id.video_player);
                videoPlayer.onVideoPause();
            }

            @Override
            public void onPageSelected(int position, boolean isBottom, View view) {
                if(mPlayPosition != position){
                    startPlayVideo(position,view);
                }
            }
        });
        return layoutManager;
    }

    private void startPlayVideo(int position,View view){
        mPlayPosition = position;
        if(!isPageVisible){
            return;
        }
        LoverVideoPlayer videoPlayer = view.findViewById(R.id.video_player);
        if(videoPlayer.getCurrentState() == GSYVideoView.CURRENT_STATE_PAUSE){
            videoPlayer.onVideoResume();
        }else{
            videoPlayer.startPlayLogic();
        }
    }

    private boolean isPlaying = false;

    private MvAdapter getMvAdapter(){
        return (MvAdapter) getMAdapter();
    }

    @Override
    protected void onLazyResume() {
        super.onLazyResume();
        isPageVisible =true;
        getEventVm().dispatchPlayWidgetEvent(false);
        getMvAdapter().onResume();
        if(PlayerManager.getInstance().isPlaying()){
            isPlaying = true;
            PlayerManager.getInstance().pauseAudio();
        }
    }


    @Override
    public void onPause() {
        if(isPlaying){
            PlayerManager.getInstance().resumeAudio();
        }
        isPageVisible = false;
        getEventVm().dispatchPlayWidgetEvent(true);
        getMvAdapter().onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        getMvAdapter().onDestroy();
        super.onDestroy();
    }
}
