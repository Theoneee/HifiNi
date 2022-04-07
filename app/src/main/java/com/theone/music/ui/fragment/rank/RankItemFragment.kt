package com.theone.music.ui.fragment.rank

import com.theone.common.constant.BundleConstant
import com.theone.common.ext.bundle
import com.theone.common.ext.getValueNonNull
import com.theone.music.net.NetConstant
import com.theone.music.ui.fragment.BaseMusicFragment
import com.theone.music.ui.fragment.MusicItemFragment
import com.theone.music.viewmodel.MusicViewModel
import com.theone.music.viewmodel.RankViewModel

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
 * @date 2022-04-07 08:50
 * @describe TODO
 * @email 625805189@qq.com
 * @remark
 */
class RankItemFragment private constructor() :
    BaseMusicFragment<RankViewModel>() {

    companion object {
        fun newInstance(type: Int): RankItemFragment = RankItemFragment().bundle {
            putInt(BundleConstant.TYPE, type)
        }
    }

    private val mType: Int by getValueNonNull(BundleConstant.TYPE)

    override fun initData() {
        getViewModel().type = mType
    }
}