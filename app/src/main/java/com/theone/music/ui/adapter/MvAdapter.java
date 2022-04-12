package com.theone.music.ui.adapter;//  ┏┓　　　┏┓
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

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder;
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack;
import com.shuyu.gsyvideoplayer.video.base.GSYBaseVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoView;
import com.theone.music.R;
import com.theone.music.data.model.MV;
import com.theone.music.databinding.ItemMvBinding;
import com.theone.music.ui.view.VideoPlayer;
import com.theone.mvvm.core.base.adapter.TheBaseQuickAdapter;

/**
 * @author The one
 * @date 2022-04-11 17:10
 * @describe TODO
 * @email 625805189@qq.com
 * @remark
 */
public class MvAdapter extends TheBaseQuickAdapter<MV, ItemMvBinding> {

    private static final String TAG = "MvAdapter";

    public MvAdapter() {
        super(R.layout.item_mv);
    }

    private VideoPlayer curPlayer;

    @Override
    protected void convert(@NonNull BaseDataBindingHolder<ItemMvBinding> holder, MV item) {
        VideoPlayer videoPlayer = holder.getView(R.id.video_player);
        videoPlayer.setVideoData(item);
        videoPlayer.setPlayTag(item.getUrl());
        videoPlayer.setPlayPosition(holder.getAbsoluteAdapterPosition());
        videoPlayer.setVideoAllCallBack(new GSYSampleCallBack() {

            @Override
            public void onPrepared(String url, Object... objects) {
                super.onPrepared(url, objects);
                curPlayer = (VideoPlayer) objects[1];
            }

            @Override
            public void onAutoComplete(String url, Object... objects) {
                super.onAutoComplete(url, objects);
                curPlayer = null;
            }
        });
    }

    public void onResume() {
        Log.e(TAG, "onResume: ");
        if (null != curPlayer) {
            GSYBaseVideoPlayer player = curPlayer.getCurrentPlayer();
            Log.e(TAG, "onResume: null != curPlayer  " + player.getCurrentState());
            if (player.getCurrentState() == GSYVideoView.CURRENT_STATE_PAUSE) {
                Log.e(TAG, "onResume: CURRENT_STATE_PAUSE ");
                player.onVideoResume();
            } else {
                player.startPlayLogic();
            }
        }
    }

    public void onPause() {
        Log.e(TAG, "onPause: ");
        if (null != curPlayer) {
            GSYBaseVideoPlayer player = curPlayer.getCurrentPlayer();
            player.onVideoPause();
        }
    }

    public void onDestroy() {
        Log.e(TAG, "onDestroy: ");
        if (null != curPlayer) {
            GSYBaseVideoPlayer player = curPlayer.getCurrentPlayer();
            player.release();
            curPlayer = null;
        }
    }

}
