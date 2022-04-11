package com.theone.music.ui.fragment.signer;//  ┏┓　　　┏┓
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

import com.theone.common.constant.BundleConstant;
import com.theone.music.data.model.Singer;
import com.theone.music.ui.fragment.BaseMusicFragment;
import com.theone.music.viewmodel.SingerSearchViewModel;

/**
 * @author The one
 * @date 2022-04-11 14:06
 * @describe TODO
 * @email 625805189@qq.com
 * @remark
 */
public class SignerSearchFragment extends BaseMusicFragment<SingerSearchViewModel> {

    private SignerSearchFragment() {
    }

    public static SignerSearchFragment newInstance(Singer singer) {
        SignerSearchFragment fragment = new SignerSearchFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(BundleConstant.DATA, singer);
        fragment.setArguments(bundle);
        return fragment;
    }

    private Singer mSinger;

    @Override
    protected String getTopBarTitle() {
        return mSinger.getName();
    }

    @Override
    public void initData() {
        mSinger = getArguments().getParcelable(BundleConstant.DATA);
        String type;
        String url = mSinger.getUrl();
        type = url.substring(url.indexOf("-") + 1, url.indexOf("."));
        getViewModel().setType(Integer.parseInt(type));
    }

}
