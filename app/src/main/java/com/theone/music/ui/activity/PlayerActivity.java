package com.theone.music.ui.activity;//  ┏┓　　　┏┓
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

import static com.qmuiteam.qmui.arch.SwipeBackLayout.DRAG_DIRECTION_NONE;
import static com.qmuiteam.qmui.arch.SwipeBackLayout.DRAG_DIRECTION_TOP_TO_BOTTOM;
import static com.zhpan.bannerview.utils.BannerUtils.dp2px;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.View;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

import com.hjq.permissions.OnPermission;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.hjq.toast.ToastUtils;
import com.kunminx.player.bean.dto.ChangeMusic;
import com.kunminx.player.bean.dto.PlayingMusic;
import com.qmuiteam.qmui.arch.SwipeBackLayout;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.theone.common.constant.BundleConstant;
import com.theone.common.widget.TheSelectImageView;
import com.theone.music.BR;
import com.theone.music.R;
import com.theone.music.data.model.CollectionEvent;
import com.theone.music.data.model.Music;
import com.theone.music.data.model.TestAlbum;
import com.theone.music.data.model.User;
import com.theone.music.databinding.PageMusicPlayerBinding;
import com.theone.music.player.PlayerManager;
import com.theone.music.service.DownloadService;
import com.theone.music.viewmodel.EventViewModel;
import com.theone.music.viewmodel.MusicInfoViewModel;
import com.theone.mvvm.base.BaseApplication;
import com.theone.mvvm.core.app.ext.LoaderExtKt;
import com.theone.mvvm.core.base.activity.BaseCoreActivity;
import com.theone.mvvm.ext.qmui.QMUIDialogExtKt;
import com.theone.mvvm.ext.qmui.QMUITipsDialogExtKt;

import java.util.List;

/**
 * @author The one
 * @date 2022-04-11 15:04
 * @describe TODO
 * @email 625805189@qq.com
 * @remark
 */
public class PlayerActivity extends BaseCoreActivity<MusicInfoViewModel, PageMusicPlayerBinding> {

    public static void startPlay(Activity activity, Music music) {
        Intent intent = new Intent(activity, PlayerActivity.class);
        intent.putExtra(BundleConstant.DATA, music);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.anim_in,0);
    }

    private EventViewModel mEvent;
    private Music mMusic;

    private boolean isTrackingTouch = false;

    protected EventViewModel getEventVm() {
        if (null == mEvent) {
            mEvent = ((BaseApplication) getApplication()).getAppViewModelProvider().get(EventViewModel.class);
        }
        return mEvent;
    }


    @Override
    public void initView(@NonNull View view) {

    }

    @Override
    public void initData() {
        // initData在createObserver之后
        mMusic = getIntent().getParcelableExtra(BundleConstant.DATA);
        if (null == mMusic) {
            return;
        }
        User user = getEventVm().getUserInfoLiveData().getValue();

        getViewModel().setLink(mMusic.shareUrl);

        PlayerManager playerManager = PlayerManager.getInstance();
        TestAlbum.TestMusic playingMusic = playerManager.getCurrentPlayingMusic();
        // TODO Step1 和当前播放的是同一个，直接拿当前的播放的数据显示
        if (null != playingMusic && playingMusic.getShareUrl().equals(mMusic.shareUrl)) {
            getViewModel().isSetSuccess().set(true);
            mMusic.pic = playingMusic.getCoverImg();
            getViewModel().setMusicInfo(mMusic, user);
            return;
        }
        // 不是同一个，就暂停上一个
        if (playerManager.isPlaying()) {
            playerManager.pauseAudio();
        }
        // 重置所有信息
        getViewModel().reset();
        String musicUrl = mMusic.getMusicUrl();
        // TODO Step2 是否有音频地址，有说明是收藏过来的
        if (!musicUrl.isEmpty()) {
            // 直接设置数据
            String cacheUrl = playerManager.getCacheUrl(musicUrl);
            getViewModel().setMediaSource(mMusic, user, !cacheUrl.isEmpty());
            return;
        }
        // TODO Step3 查询DB
        Music dbMusic = getViewModel().requestDbMusic();
        if (null != dbMusic) {
            String cacheUrl = playerManager.getCacheUrl(dbMusic.getMusicUrl());
            getViewModel().setMediaSource(dbMusic, user, !cacheUrl.isEmpty());
        }
        // TODO Step4 请求网络获取数据
        onPageReLoad();
    }

    @Override
    public void createObserver() {
        getViewModel().getResponseLiveData().observe(this, new Observer<Music>() {
            @Override
            public void onChanged(Music music) {
                LoaderExtKt.showSuccessPage(PlayerActivity.this);
                if (getViewModel().isReload()) {
                    getViewModel().setReload(false);
                    getEventVm().dispatchReloadMusic(music);
                }
                getViewModel().setMediaSource(music, getEventVm().getUserInfoLiveData().getValue(), true);
            }
        });

        getViewModel().getErrorLiveData().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                LoaderExtKt.showErrorPage(PlayerActivity.this, s, R.drawable.status_loading_view_loading_fail,
                        view -> {
                            PlayerActivity.this.onPageReLoad();
                            return null;
                        });
            }
        });

        PlayerManager playerManager = PlayerManager.getInstance();

        // 暂停事件
        playerManager.getPauseEvent().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean pause) {
                getViewModel().isPlaying().set(!pause);
            }
        });

        // 当前播放的音乐
        playerManager.getPlayingMusicEvent().observe(this, new Observer<PlayingMusic>() {
            @Override
            public void onChanged(PlayingMusic playingMusic) {
                // 拖动进度条时不再进行设值
                if (isTrackingTouch) {
                    return;
                }
                getViewModel().setMediaCourse(playingMusic);
            }
        });

        // 更改播放音乐
        playerManager.getChangeMusicEvent().observe(this, new Observer<ChangeMusic>() {
            @Override
            public void onChanged(ChangeMusic changeMusic) {
                mEvent.dispatchPlayMusic(getCurrentMusic());
            }
        });

        // 播放错误事件
        playerManager.getPlayErrorEvent().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String msg) {
                QMUITipsDialogExtKt.showFailTipsDialog(PlayerActivity.this, msg, 1000, null);
            }
        });

    }

    @Override
    public void onPageReLoad() {
        LoaderExtKt.showLoadingPage(this, "加载中");
        getViewModel().requestServer();
    }

    private Music getCurrentMusic() {
        Music cur = getViewModel().getResponseLiveData().getValue();
        if (null == cur) {
            TestAlbum.TestMusic playingMusic = PlayerManager.getInstance().getCurrentPlayingMusic();
            cur = new Music(playingMusic);
        }
        return cur;
    }

    @Override
    protected int getDragDirection(@NonNull SwipeBackLayout swipeBackLayout, @NonNull SwipeBackLayout.ViewMoveAction viewMoveAction, float downX, float downY, float dx, float dy, float slopTouch) {
        return (downY < dp2px(500) && dy >= slopTouch) ? DRAG_DIRECTION_TOP_TO_BOTTOM : DRAG_DIRECTION_NONE;
    }

    @Nullable
    @Override
    public View loaderRegisterView() {
        return getRootView();
    }

    @Nullable
    @Override
    public Object getBindingClick() {
        return new ClickProxy();
    }

    @Override
    public void applyBindingParams(@NonNull SparseArray<Object> params) {
        params.append(BR.listener, new ListenerProxy());
    }

    public class ListenerProxy implements TheSelectImageView.OnSelectChangedListener, SeekBar.OnSeekBarChangeListener {

        @Override
        public void onSelectChanged(boolean select) {
            // 收藏、添加到喜欢、取消
            // 当前是否有用户登录
            User user = getEventVm().getUserInfoLiveData().getValue();
            // 获取当前播放的数据
            Music curMusic = getCurrentMusic();
            if (null != user && null != curMusic) {
                // 一个收藏的事件
                CollectionEvent event = new CollectionEvent(select, curMusic);
                // 数据库更新收藏状态
                getViewModel().toggleCollection(user.id, event);
                // 分发一个收藏的状态
                mEvent.dispatchCollectionEvent(event);
            }
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser) {
                getViewModel().getNowTime().set(PlayerManager.getInstance().getTrackTime(progress));
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            isTrackingTouch = true;
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            isTrackingTouch = false;
            PlayerManager.getInstance().setSeek(seekBar.getProgress());
        }
    }

    public class ClickProxy {

        public void togglePlayPause() {
            PlayerManager.getInstance().togglePlay();
        }

        public void download() {
            requestPermission();
        }

    }

    /**
     * 请求外部存储权限
     */
    private void requestPermission() {
        XXPermissions.with(this)
                .permission(Permission.MANAGE_EXTERNAL_STORAGE)
                .constantRequest()
                .request(new OnPermission() {
                    @Override
                    public void hasPermission(List<String> granted, boolean all) {
                        startDownload();
                    }

                    @Override
                    public void noPermission(List<String> denied, boolean quick) {
                        if (quick) {
                            showPermissionFailTips(denied);
                        }
                    }
                });
    }

    /**
     * 开始下载
     */
    private void startDownload() {
        Music music = getCurrentMusic();
        ToastUtils.show("开始下载");
        DownloadService.start(this, music);
    }

    /**
     * 权限申请失败提示弹窗
     *
     * @param denied
     */
    private void showPermissionFailTips(List<String> denied) {
        QMUIDialog dialog = QMUIDialogExtKt.showMsgDialog(PlayerActivity.this, "提示", "权限被禁止，请在设置里打开权限",
                "取消", "开启权限", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                        if (index > 0) {
                            XXPermissions.startPermissionActivity(PlayerActivity.this, denied);
                        }
                    }
                }, QMUIDialogAction.ACTION_PROP_NEGATIVE);

        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                return keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0;
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0,R.anim.anim_out);
    }

}
