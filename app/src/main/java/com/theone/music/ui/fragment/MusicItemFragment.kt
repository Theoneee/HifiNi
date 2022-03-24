package com.theone.music.ui.fragment

import com.theone.common.constant.BundleConstant
import com.theone.common.ext.bundle
import com.theone.common.ext.getValueNonNull
import com.theone.music.net.NetConstant
import com.theone.music.viewmodel.MusicViewModel

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
 * @date 2022-01-04 11:25
 * @describe TODO
 * @email 625805189@qq.com
 * @remark
 */
class MusicItemFragment private constructor() :
    BaseMusicFragment<MusicViewModel>() {

    companion object {
        fun newInstance(type: Int): MusicItemFragment = MusicItemFragment().bundle {
            putInt(BundleConstant.TYPE, type)
        }
    }

    val mType: Int by getValueNonNull(BundleConstant.TYPE)

    override fun initData() {
        getViewModel().type = mType
        getViewModel().url = NetConstant.FORUM
    }

}