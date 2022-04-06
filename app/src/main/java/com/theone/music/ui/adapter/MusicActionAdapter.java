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
import com.theone.music.data.model.Action;
import com.theone.music.databinding.ItemActionBinding;
import com.theone.mvvm.core.base.adapter.TheBaseQuickAdapter;

/**
 * @author The one
 * @date 2022-04-06 15:37
 * @describe TODO
 * @email 625805189@qq.com
 * @remark
 */
public class MusicActionAdapter extends TheBaseQuickAdapter<Action, ItemActionBinding> {

    public MusicActionAdapter() {
        super(R.layout.item_action);
    }


}
