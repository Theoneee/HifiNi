package com.theone.music.ui.adapter

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
class MusicAdapter:TheBaseQuickAdapter<Music,ItemMusicBinding>(R.layout.item_music) {

    var currentMusic : String? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun convert(holder: BaseDataBindingHolder<ItemMusicBinding>, item: Music) {
        super.convert(holder, item)
            holder.dataBinding?.playing?.run {
                if(currentMusic == item.shareUrl){
                    visible()
                }else{
                    invisible()
                }
        }
    }


}