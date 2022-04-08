package com.theone.music.ui.adapter

import android.text.TextUtils
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.luck.picture.lib.config.PictureConfig
import com.qmuiteam.qmui.widget.QMUIRadiusImageView2
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack
import com.shuyu.gsyvideoplayer.video.base.GSYVideoView
import com.theone.common.ext.YYYY_MM_DD_HH_MM_SS
import com.theone.common.util.DateFormatUtils
import com.theone.common.widget.TheNineGridLayout
import com.theone.music.R
import com.theone.music.data.model.MV
import com.theone.music.databinding.ItemMvBinding
import com.theone.music.ui.view.LoverVideoPlayer
import com.theone.mvvm.core.base.adapter.TheBaseQuickAdapter
import com.theone.mvvm.core.ui.binding_adapter.CoreBindAdapter

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
 * @date 2022-04-08 09:22
 * @describe TODO
 * @email 625805189@qq.com
 * @remark
 */
class MvAdapter:TheBaseQuickAdapter<MV,ItemMvBinding>(R.layout.item_mv) {

    private var curPlayer: LoverVideoPlayer? = null
    private var isPlay = false

    override fun convert(holder: BaseDataBindingHolder<ItemMvBinding>, item: MV) {
        with(holder) {
                initVideoPlayer(
                    getView<LoverVideoPlayer>(R.id.video_player),
                    item,
                    holder.absoluteAdapterPosition
                )
        }
    }
    private fun initVideoPlayer(
        coverVideo: LoverVideoPlayer,
        item: MV,
        position: Int
    ) {
        with(coverVideo) {
            setVideoData(item)
            playTag = item.url
            playPosition = position
            setVideoAllCallBack(object : GSYSampleCallBack() {

                override fun onPrepared(url: String, vararg objects: Any) {
                    super.onPrepared(url, *objects)
                    curPlayer = objects[1] as LoverVideoPlayer
                    isPlay = true
                }

                override fun onAutoComplete(url: String, vararg objects: Any) {
                    super.onAutoComplete(url, *objects)
                    curPlayer = null
                    isPlay = false
                }

            })
        }
    }

    fun onResume() {
        curPlayer?.currentPlayer?.run {
            if(currentState == GSYVideoView.CURRENT_STATE_PAUSE){
                onVideoResume()
            }else{
                startPlayLogic()
            }
        }
    }

    fun onPause() {
        if (isPlay) {
            curPlayer?.currentPlayer?.onVideoPause()
        }
    }

    fun onDestroy() {
        if (isPlay) {
            curPlayer?.currentPlayer?.release()
        }
    }


}