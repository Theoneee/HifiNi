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

import androidx.databinding.DataBindingUtil;
import com.theone.music.R;
import com.theone.music.data.model.Banner;
import com.theone.music.databinding.ItemBannerBinding;
import com.zhpan.bannerview.BaseBannerAdapter;
import com.zhpan.bannerview.BaseViewHolder;

/**
 * @author The one
 * @date 2022-04-06 13:25
 * @describe TODO
 * @email 625805189@qq.com
 * @remark
 */
public class BannerAdapter extends BaseBannerAdapter<Banner> {

    @Override
    protected void bindData(BaseViewHolder<Banner> holder, Banner data, int position, int pageSize) {
        ItemBannerBinding bannerBinding = DataBindingUtil.bind(holder.itemView);
        bannerBinding.setItem(data);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_banner;
    }

}
