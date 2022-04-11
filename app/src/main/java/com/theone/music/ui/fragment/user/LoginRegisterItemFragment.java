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

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

import com.theone.common.constant.BundleConstant;
import com.theone.music.app.util.CacheUtil;
import com.theone.music.data.model.User;
import com.theone.music.databinding.FragmentLoginRegisterBinding;
import com.theone.music.ui.fragment.BaseFragment;
import com.theone.music.viewmodel.LoginRegisterViewModel;
import com.theone.mvvm.ext.qmui.QMUIFragmentExtKt;

/**
 * @author The one
 * @date 2022-04-11 14:36
 * @describe TODO
 * @email 625805189@qq.com
 * @remark
 */
public class LoginRegisterItemFragment extends BaseFragment<LoginRegisterViewModel, FragmentLoginRegisterBinding> {

    private LoginRegisterItemFragment(){}

    public static LoginRegisterItemFragment newInstance(boolean isRegister){
        LoginRegisterItemFragment fragment = new LoginRegisterItemFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(BundleConstant.TYPE,isRegister);
        fragment.setArguments(bundle);
        return fragment;
    }

    private boolean isRegister;

    @Override
    public void initData() {
        isRegister = getArguments().getBoolean(BundleConstant.TYPE);
        getViewModel().isRegister().set(isRegister);
    }

    @Override
    public void initView(@NonNull View view) {

    }

    @Override
    public void createObserver() {
        getViewModel().getResponseLiveData().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                getEventVm().setUserInfo(user);
                CacheUtil.INSTANCE.setUser(user);
                QMUIFragmentExtKt.showSuccessTipsExitDialog(LoginRegisterItemFragment.this,isRegister?"注册成功":"登录成功");
            }
        });

        getViewModel().getErrorLiveData().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                showFailTipsDialog(s);
            }
        });
    }

    private void showFailTipsDialog(String msg){
        QMUIFragmentExtKt.showFailTipsDialog(LoginRegisterItemFragment.this,msg,1000,null);
    }

    @Nullable
    @Override
    public Object getBindingClick() {
        return new ProxyClick();
    }

    public class ProxyClick{

        public void login(){

            String password = getViewModel().getPassword().get();
            String rePassword = getViewModel().getRepassword().get();

            if(getViewModel().getAccount().get().isEmpty()){
                showFailTipsDialog("请填写账号");
            }else if(password.isEmpty()){
                showFailTipsDialog("请填写密码");
            }else if(isRegister&&rePassword.isEmpty()){
                showFailTipsDialog("请填写确认密码");
            }else if(isRegister&&!password.equals(rePassword)){
                showFailTipsDialog("两次密码输入不一致");
            }else{
                getViewModel().requestServer();
            }

        }

    }

}
