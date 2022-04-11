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

import android.os.Bundle;

import com.theone.common.constant.BundleConstant;
import com.theone.music.data.constant.NetConstant;
import com.theone.music.viewmodel.MusicViewModel;

/**
 * @author The one
 * @date 2022-04-11 11:17
 * @describe TODO
 * @email 625805189@qq.com
 * @remark
 */
public class MusicItemFragment extends BaseMusicFragment<MusicViewModel> {

    private MusicItemFragment(){}

    public static MusicItemFragment newInstance(int type){
        MusicItemFragment fragment =  new MusicItemFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(BundleConstant.TYPE,type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void initData() {
        int type = getArguments().getInt(BundleConstant.TYPE);
        getViewModel().setType(type);
        getViewModel().setUrl(NetConstant.FORUM);
    }
}
