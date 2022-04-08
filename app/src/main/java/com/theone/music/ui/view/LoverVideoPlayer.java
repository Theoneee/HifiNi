package com.theone.music.ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.hjq.toast.ToastUtils;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.theone.common.ext.ViewExtKt;
import com.theone.music.R;
import com.theone.music.data.model.MV;
import com.theone.mvvm.core.app.util.FileDirectoryManager;
import java.io.File;

/**
 * @author theone
 */
public class LoverVideoPlayer extends StandardGSYVideoPlayer {

    private static final String TAG = "LoverVideoPlayer";

    ImageView mCoverImage, ivIcon;
    DouYinLoadingView mLoadingView;
    ViewGroup mTimeLayout;

    private TextView  tvNickName, tvContent;
    private boolean isClickPause = false;

    public LoverVideoPlayer(Context context) {
        super(context);
    }

    public LoverVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init(Context context) {
        super.init(context);
        setAutoFullWithSize(false);
        setReleaseWhenLossAudio(true);
        setIsTouchWiget(false);
        setLooping(true);
        setNeedShowWifiTip(false);
        setRotateViewAuto(true);
        setLockLand(false);
        setReleaseWhenLossAudio(false);
        tvNickName = findViewById(R.id.tv_nickname);
        tvContent = findViewById(R.id.tv_content);
        ivIcon = findViewById(R.id.iv_icon);
        mCoverImage = (ImageView) findViewById(R.id.thumbImage);
        mLoadingView = (DouYinLoadingView) findViewById(R.id.loading_view);
        mTimeLayout = (ViewGroup) findViewById(R.id.time_layout);
        setSpeed(1.0f);

//        mProgressAnimator = ObjectAnimator.ofInt(this, "progress", 0);
    }

    @SuppressLint("SetTextI18n")
    public void setVideoData(MV mv) {
        tvNickName.setText("@" + mv.getSinger());
        tvContent.setText(mv.getName());
        ViewExtKt.showViews(tvNickName,tvContent);
        loadCoverImage(mv.getCover());
        setUpLazy(mv.getUrl(), true, new File(getContext().getApplicationContext().getCacheDir().getAbsolutePath()+File.separator+"VideoCache"), null, "");
        changeUiToPreparingShow();
        mLoadingView.setVisibility(INVISIBLE);
     }

    private void loadCoverImage(String cover) {
        Glide.with(getContext())
                .load(cover)
                .transition(new DrawableTransitionOptions().crossFade())
                .apply(new RequestOptions()
                        .placeholder(R.drawable.image_snap_loading_bg)
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                )
                .into(mCoverImage);
    }

    public void setOnIconClickListener(OnClickListener listener) {
        ivIcon.setOnClickListener(listener);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_video_player_layout;
    }

    protected void setViewShowState(int visibility, View... views) {
        for (View view : views) {
            setViewShowState(view, visibility);
        }
    }

    @Override
    protected void hideAllWidget() {
        cancelDismissControlViewTimer();
    }

    @Override
    protected void changeUiToNormal() {
        super.changeUiToNormal();
        setViewShowState(INVISIBLE, mProgressBar, mTimeLayout, mStartButton);
        mTextureViewContainer.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.qmui_config_color_transparent));
        setViewShowState(mBottomProgressBar, VISIBLE);
    }

    @Override
    protected void changeUiToPreparingShow() {
        setViewShowState(VISIBLE, mLoadingView, mBottomContainer);
        setViewShowState(INVISIBLE, mBottomProgressBar, mProgressBar, mTimeLayout);
        mLoadingView.start();
    }

    @Override
    protected void changeUiToPlayingClear() {
        setViewShowState(VISIBLE, mProgressBar, mTimeLayout, mStartButton);
        setViewShowState(INVISIBLE, mBottomContainer, mBottomProgressBar, mLoadingView);
    }

    @Override
    protected void changeUiToPlayingShow() {
        if (isClickPause) {
            isClickPause = false;
            if (mTimeLayout.getVisibility() == VISIBLE) {
                updateStartImage();
                return;
            }
        }
        super.changeUiToPlayingShow();
        mLoadingView.stop();
        setViewShowState(VISIBLE, mBottomContainer, mBottomProgressBar);
        setViewShowState(INVISIBLE, mStartButton, mLoadingView, mTimeLayout, mProgressBar);
        // 封面一直是存在的，这样不会因为封面的消失而视频播放界面还没出现时出现的黑屏现象
        // 有时会存在封面的尺寸和视频的尺寸不一致，视频之后还能看见封面，所以这个时候要延时将播放界面背景设置成黑色
        postDelayed(() ->
                        mTextureViewContainer.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.black))
                , 500);

    }

    @Override
    protected void changeUiToPauseShow() {
        setViewShowState(VISIBLE, mStartButton);
        updateStartImage();
        updatePauseCover();
        isClickPause = true;
    }

    @Override
    protected void changeUiToPlayingBufferingShow() {
        super.changeUiToPlayingBufferingShow();
        setViewShowState(INVISIBLE, mBottomProgressBar, mProgressBar);
        setViewShowState(VISIBLE, mLoadingView);
        mLoadingView.start();
    }

    @Override
    protected void changeUiToPlayingBufferingClear() {
        super.changeUiToPlayingBufferingClear();
        setViewShowState(VISIBLE, mBottomProgressBar);
    }

    @Override
    protected void changeUiToError() {
        super.changeUiToError();
        mLoadingView.stop();
        setViewShowState(INVISIBLE, mLoadingView);
        ToastUtils.show("error");
    }
}
