package com.theone.music.ui.fragment.signer

import android.util.Log
import com.theone.common.constant.BundleConstant
import com.theone.common.ext.bundle
import com.theone.common.ext.getNumbers
import com.theone.common.ext.getValueNonNull
import com.theone.common.ext.matchResult
import com.theone.music.data.model.Singer
import com.theone.music.ui.fragment.BaseMusicFragment
import com.theone.music.ui.fragment.rank.RankItemFragment
import com.theone.music.viewmodel.RankViewModel
import com.theone.music.viewmodel.SingerSearchViewModel
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
 * @date 2022-04-07 09:46
 * @describe 歌
 * @email 625805189@qq.com
 * @remark
 */
class SignerSearchFragment private constructor() :
    BaseMusicFragment<SingerSearchViewModel>() {

    companion object {
        fun newInstance(singer: Singer): SignerSearchFragment = SignerSearchFragment().bundle {
            putParcelable(BundleConstant.DATA, singer)
        }
    }

    private val mSinger: Singer by getValueNonNull(BundleConstant.DATA)

    override fun initData() {
        setTitleWitchBackBtn(mSinger.name)
        val type = mSinger.url.run {
            substring(indexOf("-") + 1, indexOf("."))
        }
        getViewModel().type = type.toInt()
    }
}