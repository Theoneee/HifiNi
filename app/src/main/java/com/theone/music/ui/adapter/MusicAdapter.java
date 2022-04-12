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

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder;
import com.theone.music.R;
import com.theone.music.data.model.Music;
import com.theone.music.databinding.ItemMusicBinding;
import com.theone.mvvm.core.base.adapter.TheBaseQuickAdapter;

/**
 * @author The one
 * @date 2022-04-11 17:21
 * @describe 音乐列表适配器
 * @email 625805189@qq.com
 * @remark
 */
public class MusicAdapter extends TheBaseQuickAdapter<Music, ItemMusicBinding> {

    public MusicAdapter() {
        super(R.layout.item_music);
        setDiffCallback(new DiffUtil.ItemCallback<Music>() {
            @Override
            public boolean areItemsTheSame(@NonNull Music oldItem, @NonNull Music newItem) {
                return oldItem.shareUrl.equals(newItem.shareUrl);
            }

            @Override
            public boolean areContentsTheSame(@NonNull Music oldItem, @NonNull Music newItem) {
                return oldItem.shareUrl.equals(newItem.shareUrl);
            }
        });
    }

    private Music currentMusic;

    public void setCurrentMusic(Music current) {
        String currentUrl = current.shareUrl;
        String oldUrl = "";
        if (null != currentMusic) {
            oldUrl = currentMusic.shareUrl;
        }
        int old = -1;
        int now = -1;
        for (int i = 0; i < getData().size(); i++) {
            Music item = getData().get(i);
            // 上一个播放的位置
            if (item.shareUrl.equals(oldUrl)) {
                old = i;
            }
            // 现在需要更改的位置
            if (item.shareUrl.equals(currentUrl)) {
                now = i;
            }
            // 两个都找出来了退出循环
            if(old != -1 && now != -1){
                break;
            }
        }
        currentMusic = current;
        notifyItem(old, now);
    }

    private void notifyItem(int... values) {
        // 这个方法改变的是所有的数据
        // notifyDataSetChanged()
        for (int index : values) {
            if (index != -1) {
                // 只刷新一条
                notifyItemChanged(index + getHeaderLayoutCount());
            }
        }
    }

    @Override
    protected void convert(@NonNull BaseDataBindingHolder<ItemMusicBinding> holder, Music item) {
        super.convert(holder, item);
        holder.getDataBinding().playing.setVisibility(null !=currentMusic&&currentMusic.shareUrl.equals(item.shareUrl)? View.VISIBLE:View.GONE);
    }
}
