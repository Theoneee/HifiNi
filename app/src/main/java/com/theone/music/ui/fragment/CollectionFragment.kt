package com.theone.music.ui.fragment

import com.theone.music.app.ext.removeItem
import com.theone.music.viewmodel.CollectionViewModel
import com.theone.music.viewmodel.EventViewModel
import com.theone.mvvm.ext.getAppViewModel

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

    override fun createObserver() {
        super.createObserver()
        mEvent.getCollectionLiveData().observeInFragment(this){
            if(it.collection){
                onAutoRefresh()
            }else{
                mAdapter.run {
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

    override fun onLoadMoreComplete() {
        onLoadMoreEnd()
    }

}