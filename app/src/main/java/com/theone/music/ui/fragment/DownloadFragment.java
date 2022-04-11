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

import android.view.View;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.theone.music.data.model.DownloadResult;
import com.theone.music.ui.adapter.DownloadAdapter;
import com.theone.music.viewmodel.DownloadViewModel;

/**
 * @author The one
 * @date 2022-04-11 10:59
 * @describe 我的下载
 * @email 625805189@qq.com
 * @remark
 */
public class DownloadFragment extends BasePagerFragment<DownloadResult, DownloadViewModel> {

    @Override
    protected String getTopBarTitle() {
        return "我的下载";
    }

    @NonNull
    @Override
    public BaseQuickAdapter<DownloadResult, ?> createAdapter() {
        return new DownloadAdapter();
    }

    @Override
    public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {

    }
}
