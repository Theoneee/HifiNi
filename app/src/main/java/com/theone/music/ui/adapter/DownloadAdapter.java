package com.theone.music.ui.adapter;//  ┏┓　　　┏┓
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

import com.theone.music.R;
import com.theone.music.data.model.DownloadResult;
import com.theone.music.databinding.ItemDownloadBinding;
import com.theone.mvvm.core.base.adapter.TheBaseQuickAdapter;

/**
 * @author The one
 * @date 2022-04-11 17:20
 * @describe TODO
 * @email 625805189@qq.com
 * @remark
 */
public class DownloadAdapter extends TheBaseQuickAdapter<DownloadResult, ItemDownloadBinding> {
    public DownloadAdapter() {
        super(R.layout.item_download);
    }
}
