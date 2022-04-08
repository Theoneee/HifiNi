package com.theone.music.ui.fragment

import android.view.View
import com.theone.music.viewmodel.HistoryViewModel
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
 * @date 2022-04-07 17:21
 * @describe TODO
 * @email 625805189@qq.com
 * @remark
 */
class HistoryFragment:BaseMusicFragment<HistoryViewModel>() {

    override fun initView(root: View) {
        super.initView(root)
        setTitleWitchBackBtn("播放记录")
    }
}