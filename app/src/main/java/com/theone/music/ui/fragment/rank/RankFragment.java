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

import androidx.annotation.NonNull;

import com.qmuiteam.qmui.arch.QMUIFragment;
import com.theone.music.data.constant.NetConstant;
import com.theone.mvvm.base.viewmodel.BaseViewModel;
import com.theone.mvvm.core.base.fragment.BaseTabInTitleFragment;
import com.theone.mvvm.core.data.entity.QMUITabBean;
import com.theone.mvvm.ext.qmui.QMUITopBarLayoutExtKt;

import java.util.List;
import java.util.Map;

/**
 * @author The one
 * @date 2022-04-11 14:34
 * @describe TODO
 * @email 625805189@qq.com
 * @remark
 */
public class RankFragment extends BaseTabInTitleFragment<BaseViewModel> {

    @Override
    protected void initTopBar() {
        QMUITopBarLayoutExtKt.addLeftCloseImageBtn(this,-1);
    }

    @Override
    public void initTabAndFragments(@NonNull List<QMUITabBean> tabs, @NonNull List<QMUIFragment> fragments) {
        for (Map.Entry<String, Integer> entry : NetConstant.getRankTypes().entrySet()) {
            tabs.add(new QMUITabBean(entry.getKey(), -1, -1));
            fragments.add(RankItemFragment.newInstance(entry.getValue()));
        }
    }


}
