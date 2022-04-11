package com.theone.music.ui.fragment.rank;//  ┏┓　　　┏┓
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
import com.theone.music.ui.fragment.BaseMusicFragment;
import com.theone.music.viewmodel.RankViewModel;

/**
 * @author The one
 * @date 2022-04-11 14:31
 * @describe TODO
 * @email 625805189@qq.com
 * @remark
 */
public class RankItemFragment extends BaseMusicFragment<RankViewModel> {

    private RankItemFragment(){}

    public static RankItemFragment newInstance(int type){
        RankItemFragment fragment = new RankItemFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(BundleConstant.TYPE,type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void initData() {
        int type = getArguments().getInt(BundleConstant.TYPE);
        getViewModel().setType(type);
    }
}
