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

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.PARENT_ID;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentContainerView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.kunminx.player.bean.dto.ChangeMusic;
import com.qmuiteam.qmui.arch.annotation.DefaultFirstFragment;
import com.qmuiteam.qmui.layout.QMUIConstraintLayout;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.QMUIWindowInsetLayout2;
import com.theone.common.widget.TheSelectImageView;
import com.theone.music.R;
import com.theone.music.app.ext.AppExtKt;
import com.theone.music.data.model.CollectionEvent;
import com.theone.music.data.model.Music;
import com.theone.music.data.model.TestAlbum;
import com.theone.music.data.model.User;
import com.theone.music.databinding.MusicPlayerLayoutBinding;
import com.theone.music.player.PlayerManager;
import com.theone.music.ui.fragment.IndexFragment;
import com.theone.music.viewmodel.EventViewModel;
import com.theone.music.viewmodel.MusicInfoViewModel;
import com.theone.mvvm.base.BaseApplication;
import com.theone.mvvm.base.activity.BaseFragmentActivity;
import com.theone.mvvm.ext.qmui.QMUITipsDialogExtKt;

/**
 * @author The one
 * @date 2022-04-11 09:01
 * @describe TODO
 * @email 625805189@qq.com
 * @remark
 */
@DefaultFirstFragment(IndexFragment.class)
public class MainActivity extends BaseFragmentActivity {

    private EventViewModel mEvent;
    private MusicInfoViewModel mMusicViewModel;
    private MusicPlayerLayoutBinding mPlayLayout;
    private int playerLayoutHeight;
    private Music mPlayingMusic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mEvent = ((BaseApplication) getApplication()).getAppViewModelProvider().get(EventViewModel.class);
        mMusicViewModel = new ViewModelProvider(this).get(MusicInfoViewModel.class);
        playerLayoutHeight = QMUIDisplayHelper.dp2px(this, 60);
        createEventObserve();
        createPlayerObserve();
    }

    private void createEventObserve() {
        // 全局的收藏消息
        mEvent.getCollectionLiveData().observe(this, new Observer<CollectionEvent>() {
            @Override
            public void onChanged(CollectionEvent event) {
                mMusicViewModel.isCollection().set(event.getCollection());
            }
        });
        // 用户登录、退出后的消息
        mEvent.getUserInfoLiveData().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                // 是否可以点击收藏
                boolean isLogin = user != null;
                mMusicViewModel.isCollectionEnable().set(isLogin);
                // 先默认设置为false
                mMusicViewModel.isCollection().set(false);
                // 登录后当前有播放，查询当前用户是否已收藏当前的播放
                if (isLogin) {
                    Music music = getCurrentMusic();
                    if (null != music) {
                        mMusicViewModel.requestCollection(user.getId(), music.getShareUrl());
                    }
                }
            }
        });

        // 底部播放组件是否显示(主要用于MVFragment与SearchFragment之间的跳转）
        mEvent.getPlayWidgetLiveData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean show) {
                QMUIConstraintLayout playerLayout = mPlayLayout.musicPlayerLayout;
                playerLayout.setVisibility(show ? View.VISIBLE : View.GONE);
                int height = show ? playerLayoutHeight : 0;
                // 如果和当前的高度相同就不做任何改变
                if (playerLayout.getLayoutParams().height == height) {
                    return;
                }
                playerLayout.setLayoutParams(generatePlayLayoutParams(height));
            }
        });
        // ViewPager滑动变化，更改PlayLayout的高度
        mEvent.getPlayWidgetAlphaLiveData().observe(this, new Observer<Float>() {
            @Override
            public void onChanged(Float percent) {
                QMUIConstraintLayout playerLayout = mPlayLayout.musicPlayerLayout;
                // 根据百分比计算出高度
                int height = (int)(playerLayoutHeight * (1 - percent));
                // 如果和当前的高度相同就不做任何改变
                if(height == playerLayout.getLayoutParams().height){
                    return;
                }
                if(height == 0){
                    playerLayout.setVisibility(View.GONE);
                    return;
                }
                playerLayout.setVisibility(View.VISIBLE);
                playerLayout.setLayoutParams(generatePlayLayoutParams(height));
            }
        });
    }

    private void createPlayerObserve(){
        PlayerManager playerManager = PlayerManager.getInstance();
        // 暂停事件
        playerManager.getPauseEvent().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isPause) {
                mMusicViewModel.isPlaying().set(isPause);
            }
        });

        // 切换歌曲事件
        playerManager.getChangeMusicEvent().observe(this, new Observer<ChangeMusic>() {
            @Override
            public void onChanged(ChangeMusic changeMusic) {
                TestAlbum.TestMusic music = (TestAlbum.TestMusic) changeMusic.getMusic();
                // 设置数据
                mMusicViewModel.getCover().set(music.getCoverImg());
                mMusicViewModel.getName().set(music.getTitle());
                mMusicViewModel.getAuthor().set(music.getAuthor());
                mMusicViewModel.isSuccess().set(true);

                // 如果是登录的，查询当前用户是否已收藏当前的播放
                User user = mEvent.getUserInfoLiveData().getValue();
                if(null != user){
                    mMusicViewModel.requestCollection(user.getId(),music.getShareUrl());
                }
            }
        });

        // 播放错误事件
        playerManager.getPlayErrorEvent().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String msg) {
                QMUITipsDialogExtKt.showFailTipsDialog(getBaseContext(),msg,1000,null);
            }
        });
    }

    /**
     * 生成播放组件的LayoutParams
     * @param height 高度
     * @return LayoutParams
     */
    private ConstraintLayout.LayoutParams generatePlayLayoutParams(int height) {
        ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
        lp.startToStart = PARENT_ID;
        lp.endToEnd = PARENT_ID;
        lp.bottomToBottom = PARENT_ID;
        return lp;
    }

    /**
     * @return 获取当前播放的
     */
    private Music getCurrentMusic() {
        TestAlbum.TestMusic music = PlayerManager.getInstance().getCurrentPlayingMusic();
        if (null == music) {
            return null;
        } else {
            // 这里做一个局部的缓存，不用每次都去创建一个对象
            if (null != mPlayingMusic && !mPlayingMusic.getShareUrl().equals(music.getShareUrl())) {
                // TestAlbum.TestMusic -> Music
                mPlayingMusic = AppExtKt.toMusic(music);
            }
            return mPlayingMusic;
        }
    }

    public class ClickProxy implements TheSelectImageView.OnSelectChangedListener{

        public void togglePlayPause(){
            PlayerManager.getInstance().togglePlay();
        }

        public void jumpPlayerActivity(){
            Music curMusic = getCurrentMusic();
            if(null != curMusic){

            }
        }

        @Override
        public void onSelectChanged(boolean select) {
            User user = mEvent.getUserInfoLiveData().getValue();
            Music curMusic = getCurrentMusic();
            if(null != user && null != curMusic){
                CollectionEvent event = new CollectionEvent(select,curMusic);
                mMusicViewModel.toggleCollection(user.getId(),event);
                mEvent.dispatchCollectionEvent(event);
            }
        }

    }

    @Override
    protected RootView onCreateRootView(int fragmentContainerId) {
        return new CustomRootView(this,fragmentContainerId);
    }

    /**
     * 自定义RootView
     */
    private class CustomRootView extends RootView{

        private FragmentContainerView fragmentContainer;

        public CustomRootView(Context context, int fragmentContainerId) {
            super(context, fragmentContainerId);
            fragmentContainer = new FragmentContainerView(context);
            fragmentContainer.setId(fragmentContainerId);

            mPlayLayout = MusicPlayerLayoutBinding.inflate(getLayoutInflater());
            mPlayLayout.setLifecycleOwner(MainActivity.this);
            mPlayLayout.setVm(mMusicViewModel);
            mPlayLayout.setProxy(new ClickProxy());
            QMUIConstraintLayout root = (QMUIConstraintLayout) mPlayLayout.getRoot();
            root.updateTopDivider(0,0,1, ContextCompat.getColor(context, R.color.qmui_config_color_separator));

            ConstraintLayout.LayoutParams contentLP = new ConstraintLayout.LayoutParams(MATCH_PARENT,0);
            contentLP.startToStart = PARENT_ID;
            contentLP.endToEnd = PARENT_ID;
            contentLP.topToTop = PARENT_ID;
            contentLP.bottomToTop = R.id.music_player_layout;

            QMUIWindowInsetLayout2 insetLayout = new QMUIWindowInsetLayout2(context);
            insetLayout.addView(fragmentContainer,contentLP);
            insetLayout.addView(root,generatePlayLayoutParams(playerLayoutHeight));

            addView(insetLayout,new ViewGroup.LayoutParams(MATCH_PARENT,MATCH_PARENT));
        }

        @Override
        public FragmentContainerView getFragmentContainerView() {
            return fragmentContainer;
        }

    }

}
