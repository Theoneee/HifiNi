package com.theone.music.ui.fragment

import android.view.View
import com.theone.music.app.ext.removeItem
import com.theone.music.viewmodel.CollectionViewModel
import com.theone.music.viewmodel.EventViewModel
import com.theone.mvvm.core.app.ext.showEmptyPage
import com.theone.mvvm.ext.getAppViewModel
import com.theone.mvvm.ext.qmui.setTitleWitchBackBtn

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
 * @date 2022-01-06 13:41
 * @describe TODO
 * @email 625805189@qq.com
 * @remark
 */
class CollectionFragment: BaseMusicFragment<CollectionViewModel>() {

    private val mEvent: EventViewModel by lazy { getAppViewModel<EventViewModel>() }

    override fun initView(root: View) {
        setTitleWitchBackBtn("我的收藏")
        super.initView(root)
    }

    override fun initData() {
        super.initData()
        mEvent.getUserInfoLiveData().value?.let {
            getViewModel().username = it.account
        }
    }

    override fun setRefreshLayoutEnabled(enabled: Boolean) {
        super.setRefreshLayoutEnabled(false)
    }

    override fun createObserver() {
        super.createObserver()
        mEvent.getCollectionLiveData().observe(this){
            if(it.collection){
                onAutoRefresh()
            }else{
                getAdapter().run {
                    if(data.size == 1){
                        showEmptyPage()
                    }else{
                        for ((index,item) in data.withIndex()){
                            if(item.shareUrl == it.music.shareUrl){
                                removeItem(index)
                                break
                            }
                        }
                    }
                }
            }
        }

        mEvent.getReloadMusicLiveData().observe(this){
            with(getAdapter()){
                for ((index,item) in data.withIndex()){
                    if(item.shareUrl == it.shareUrl){
                        item.playUrl = it.playUrl
                        item.realUrl = it.realUrl
                        break
                    }
                }
            }
        }
    }

}