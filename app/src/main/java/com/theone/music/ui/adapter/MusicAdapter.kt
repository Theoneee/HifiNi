package com.theone.music.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.theone.common.ext.gone
import com.theone.common.ext.invisible
import com.theone.common.ext.visible
import com.theone.music.R
import com.theone.music.data.model.Music
import com.theone.music.databinding.ItemMusicBinding
import com.theone.mvvm.core.base.adapter.TheBaseQuickAdapter

//  ┏┓　　　┏┓
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
/**
 * @author The one
 * @date 2022-01-04 14:15
 * @describe TODO
 * @email 625805189@qq.com
 * @remark
 */
class MusicAdapter : TheBaseQuickAdapter<Music, ItemMusicBinding>(R.layout.item_music) {

    init {

        val callBack: DiffUtil.ItemCallback<Music> = object : DiffUtil.ItemCallback<Music>() {

            override fun areItemsTheSame(oldItem: Music, newItem: Music): Boolean =
                oldItem.shareUrl == newItem.shareUrl

            override fun areContentsTheSame(oldItem: Music, newItem: Music): Boolean =
                oldItem.realUrl == newItem.realUrl

        }
        setDiffCallback(callBack)
    }

    var currentMusic: String? = null
        set(value) {
            // 记录旧的播放位置
            var old = -1
            // 这里是现在播放的位置
            var new = -1
            // 遍历当前所有的数据
            for ((index, item) in data.withIndex()) {
                // 和当前播放的数据做比较
                if (item.shareUrl == currentMusic) {
                    // 如果相同的
                    old = index
                }
                if (item.shareUrl == value) {
                    new = index
                }
            }
            field = value
            notifyItem(old,new)
        }

    /**
     * 改变Item
     */
    private fun notifyItem(vararg values: Int){
        // 这个方法改变的是所有的数据
        // notifyDataSetChanged()
        for (index in values){
            if (index != -1)
                // 只刷新一条
                notifyItemChanged(index+headerLayoutCount)
        }
    }

    override fun convert(holder: BaseDataBindingHolder<ItemMusicBinding>, item: Music) {
        super.convert(holder, item)
        holder.dataBinding?.playing?.run {
            if (currentMusic == item.shareUrl) {
                visible()
            } else {
                gone()
            }
        }
    }


}