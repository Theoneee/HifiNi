package com.theone.music.ui.fragment.music

import android.graphics.Color
import android.util.Log
import android.view.View
import com.hw.lrcviewlib.LrcDataBuilder
import com.qmuiteam.qmui.util.QMUIDisplayHelper
import com.qmuiteam.qmui.util.QMUIResHelper
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet
import com.theone.common.ext.getColor
import com.theone.common.ext.notNull
import com.theone.music.R
import com.theone.music.data.model.Lrc
import com.theone.music.databinding.FragmentLrcBinding
import com.theone.music.net.LrcUrl
import com.theone.music.player.PlayerManager
import com.theone.music.viewmodel.LrcViewModel
import com.theone.mvvm.core.app.ext.qmui.OnGridBottomSheetItemClickListener
import com.theone.mvvm.core.app.ext.qmui.showBottomListSheet
import com.theone.mvvm.core.app.ext.showErrorPage
import com.theone.mvvm.core.app.ext.showLoadingPage
import com.theone.mvvm.core.app.ext.showSuccessPage
import com.theone.mvvm.core.base.fragment.BaseCoreFragment
import com.theone.mvvm.core.base.loader.callback.LoadingCallback
import com.theone.mvvm.ext.qmui.showSingleChoiceDialog
import java.io.File

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
 * @date 2022-06-15 09:00
 * @describe TODO
 * @email 625805189@qq.com
 * @remark
 */
class LrcFragment : BaseCoreFragment<LrcViewModel, FragmentLrcBinding>() {

    override fun loaderRegisterView() = getContentView()

    override fun loaderDefaultCallback() = LoadingCallback::class.java

    override fun showTopBar(): Boolean = false

    override fun initView(root: View) {
        initLrcView()
        PlayerManager.getInstance().currentPlayingMusic.run {
            getViewModel().musicName.set(title)
            getViewModel().musicSinger.set(author)
            getViewModel().isLrcExists()
            getViewModel().requestServer()
        }
    }

    private fun initLrcView() {
        getDataBinding().lrcView.run {
            setDraggable(true){
                PlayerManager.getInstance().setSeek(it.toInt())
                true
            }
        }
    }

    override fun createObserver() {
        getViewModel().getLrcPathLiveData().observe(this) {
            setLrcData(it)
        }
        getViewModel().getErrorLiveData().observe(this) {
            if(getViewModel().getLrcPathLiveData().value.isNullOrEmpty()){
                showErrorPage(it) {
                    showLoadingPage()
                    getViewModel().requestServer()
                }
            }
        }
        getViewModel().getLrcLiveData().observe(this) {
            it.notNull({ lrc ->
                getViewModel().downloadLrc(lrc.url)
            }, {
                showErrorPage("没有找到匹配歌词")
            })
        }
        with(PlayerManager.getInstance()){
            // 暂停事件
            pauseEvent.observe(this@LrcFragment) {
                getViewModel().isPlaying.set(!it)
            }

            playingMusicEvent.observe(this@LrcFragment){
                getDataBinding().lrcView.updateTime(it.playerPosition.toLong())
            }
        }
    }

    private fun setLrcData(path: String) {
        getDataBinding().lrcView.loadLrc(File(path))
        showSuccessPage()
    }

    override fun getBindingClick(): Any = ClickProxy()

    inner class ClickProxy {

        fun togglePlayPause() {
            PlayerManager.getInstance().togglePlay()
        }

        fun lrcChange(){
           getViewModel().getResponseLiveData().value?.let { list->
               val curLrc = getViewModel().getLrcLiveData().value
               mActivity.showBottomListSheet(list,"歌词列表", markContent = curLrc?.getItemTitle().toString(), listener = object :OnGridBottomSheetItemClickListener<Lrc>{
                   override fun onGridBottomSheetItemClick(
                       dialog: QMUIBottomSheet,
                       itemView: View,
                       item: Lrc
                   ) {
                       dialog.dismiss()
                       if(item != curLrc){
                           getViewModel().changeLrc(item)
                       }
                   }
               }).show()
            }
        }

    }

}