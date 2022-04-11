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

import androidx.databinding.ViewDataBinding;

import com.theone.music.viewmodel.EventViewModel;
import com.theone.mvvm.base.BaseApplication;
import com.theone.mvvm.base.viewmodel.BaseViewModel;
import com.theone.mvvm.core.base.fragment.BaseCoreFragment;

/**
 * @author The one
 * @date 2022-04-11 13:46
 * @describe TODO
 * @email 625805189@qq.com
 * @remark
 */
public abstract class BaseFragment<VM extends BaseViewModel, DB extends ViewDataBinding> extends BaseCoreFragment<VM,DB> {

    private EventViewModel mEvent;

    protected EventViewModel getEventVm() {
        if (null == mEvent) {
            mEvent = ((BaseApplication) mActivity.getApplication()).getAppViewModelProvider().get(EventViewModel.class);
        }
        return mEvent;
    }

}
