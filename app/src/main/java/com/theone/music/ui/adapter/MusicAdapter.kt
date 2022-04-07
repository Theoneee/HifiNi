package com.theone.music.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
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
            var old = -1
            var new = -1
            for ((index, item) in data.withIndex()) {
                if (item.shareUrl == currentMusic) {
                    old = index
                }
                if (item.shareUrl == value) {
                    new = index
                }
            }
            field = value
            notifyItem(old,new)
        }

    private fun notifyItem(vararg values: Int){
        for (index in values){
            if (index != -1)
                notifyItemChanged(index+headerLayoutCount)
        }
    }

    override fun convert(holder: BaseDataBindingHolder<ItemMusicBinding>, item: Music) {
        super.convert(holder, item)
        holder.dataBinding?.playing?.run {
            if (currentMusic == item.shareUrl) {
                visible()
            } else {
                invisible()
            }
        }
    }


}