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

import static com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON;
import static com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView.HORIZONTAL;

import android.view.View;

import androidx.annotation.NonNull;

import com.qmuiteam.qmui.util.QMUIPackageHelper;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;
import com.theone.music.R;
import com.theone.music.app.util.CacheUtil;
import com.theone.music.databinding.FragmentSettingBinding;
import com.theone.mvvm.base.viewmodel.BaseViewModel;
import com.theone.mvvm.ext.qmui.QMUICommonListItemViewExtKt;
import com.theone.mvvm.ext.qmui.QMUIDialogExtKt;
import com.theone.mvvm.ext.qmui.QMUIFragmentExtKt;

/**
 * @author The one
 * @date 2022-04-11 13:45
 * @describe 设置
 * @email 625805189@qq.com
 * @remark
 */
public class SettingFragment extends BaseFragment<BaseViewModel, FragmentSettingBinding> implements View.OnClickListener {

    private QMUICommonListItemView mVersion, mLoginOut;

    @Override
    public void initView(@NonNull View view) {
        QMUIGroupListView groupListView = getDataBinding().groupListView;
        mVersion = QMUICommonListItemViewExtKt.createItem(groupListView,
                "当前版本",
                "Ver " + QMUIPackageHelper.getAppVersion(mActivity),
                R.drawable.svg_setting_version, HORIZONTAL, ACCESSORY_TYPE_CHEVRON);

        QMUICommonListItemViewExtKt.addToGroup(groupListView, new QMUICommonListItemView[]{mVersion}, "关于", null, null);

        mLoginOut = QMUICommonListItemViewExtKt.createItem(groupListView,
                "退出账号",
                null,
                R.drawable.svg_setting_version, HORIZONTAL, ACCESSORY_TYPE_CHEVRON);

        if (CacheUtil.INSTANCE.isLogin()) {
            QMUICommonListItemViewExtKt.addToGroup(groupListView, new QMUICommonListItemView[]{mLoginOut}, "关于", null, this);
        }

    }


    @Override
    public void onClick(View v) {
        QMUIDialogExtKt.showMsgDialog(mActivity, "提示", "是否退出当前账号", "取消", "确定", new QMUIDialogAction.ActionListener() {
            @Override
            public void onClick(QMUIDialog dialog, int index) {
                dialog.dismiss();
                if (index > 0) {
                    getEventVm().setUserInfo(null);
                    CacheUtil.INSTANCE.loginOut();
                    QMUIFragmentExtKt.showSuccessTipsExitDialog(SettingFragment.this, "退出成功");
                }
            }
        }, QMUIDialogAction.ACTION_PROP_NEGATIVE);
    }


}
