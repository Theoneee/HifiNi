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

import android.content.Intent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

import com.theone.music.app.util.CacheUtil;
import com.theone.music.data.model.User;
import com.theone.music.databinding.FragmentMineBinding;
import com.theone.music.ui.activity.LoginRegisterActivity;
import com.theone.music.viewmodel.MineViewModel;

/**
 * @author The one
 * @date 2022-04-11 13:35
 * @describe 我的
 * @email 625805189@qq.com
 * @remark
 */
public class MineFragment extends BaseFragment<MineViewModel, FragmentMineBinding> {

    @Override
    public boolean isNeedChangeStatusBarMode() {
        return true;
    }

    @Override
    public boolean isStatusBarLightMode() {
        return true;
    }

    @Override
    public boolean showTopBar() {
        return true;
    }

    @Override
    public void initView(@NonNull View view) {
        User user = getEventVm().getUserInfoLiveData().getValue();
        setUserInfo(user);
    }

    @Override
    public void createObserver() {
        super.createObserver();
        getEventVm().getUserInfoLiveData().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                setUserInfo(user);
            }
        });
    }

    private void setUserInfo(User user) {
        String nickName = null == user ? "未登录" : user.getAccount();
        getViewModel().nickName.set(nickName);
    }

    private interface LoginCallback {

        void onLogin();

    }


    public void checkLogin(LoginCallback callback) {
        if (CacheUtil.INSTANCE.isLogin()) {
            if (null != callback) {
                callback.onLogin();
            }
        } else {
            startActivity(new Intent(mActivity, LoginRegisterActivity.class));
        }
    }

    @Nullable
    @Override
    public Object getBindingClick() {
        return new ClickProxy();
    }

    public class ClickProxy {

        public void login() {
            checkLogin(new LoginCallback() {
                @Override
                public void onLogin() {

                }
            });
        }

        public void icon() {
            checkLogin(new LoginCallback() {
                @Override
                public void onLogin() {

                }
            });
        }

        public void setting() {
            startFragment(new SettingFragment());
        }

        public void collection() {
            checkLogin(new LoginCallback() {
                @Override
                public void onLogin() {
                    startFragment(new CollectionFragment());
                }
            });
        }

        public void history() {
            startFragment(new HistoryFragment());
        }

        public void download() {
            startFragment(new DownloadFragment());
        }


    }

}
