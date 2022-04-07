package com.theone.music.ui.fragment.signer

import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.qmuiteam.qmui.kotlin.wrapContent
import com.qmuiteam.qmui.layout.QMUIFrameLayout
import com.theone.common.ext.dp2px
import com.theone.common.ext.getDrawable
import com.theone.music.R
import com.theone.music.app.util.ColorUtil
import com.theone.music.data.model.Singer
import com.theone.music.databinding.FragmentSingerBinding
import com.theone.music.viewmodel.SingerViewModel
import com.theone.mvvm.core.app.ext.showErrorPage
import com.theone.mvvm.core.app.ext.showLoadingPage
import com.theone.mvvm.core.app.ext.showSuccessPage
import com.theone.mvvm.core.base.fragment.BaseCoreFragment
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
 * @date 2022-04-07 09:09
 * @describe TODO
 * @email 625805189@qq.com
 * @remark
 */
class SingerFragment : BaseCoreFragment<SingerViewModel, FragmentSingerBinding>() {

    override fun loaderRegisterView(): View = getContentView()

    override fun initView(root: View) {
        setTitleWitchBackBtn("歌手")
        onPageReLoad()
    }

    override fun onPageReLoad() {
        showLoadingPage()
        getViewModel().requestServer()
    }

    override fun createObserver() {
        getViewModel().getResponseLiveData().observe(this) {
            layoutSinger(it)
            showSuccessPage()
        }
        getViewModel().getErrorLiveData().observe(this) {
            showErrorPage(it) {
                onPageReLoad()
            }
        }
    }

    private fun layoutSinger(singers: List<Singer>) {
        getDataBinding().floatLayout
            .run {
                removeAllViews()
                for (singer in singers) {
                    val layoutParams = ViewGroup.LayoutParams(wrapContent, wrapContent)
                    val container = QMUIFrameLayout(context)
                    val space = context.dp2px(10)
                    container.setPadding(0, 0, space, space)
                    val tag = TextView(context)
                    val padding = context.dp2px(4)
                    val padding2 = padding * 2
                    tag.run {
                        setPadding(padding2, padding, padding2, padding)
                        setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f)
                        maxLines = 1
                        setTextColor(ColorUtil.randomColor())
                        text = singer.name
                        background = getDrawable(
                            context,
                            R.drawable.tree_tag_bg
                        )
                    }
                    container.addView(tag, layoutParams)
                    container.setOnClickListener {
                        startFragment(SignerSearchFragment.newInstance(singer))
                    }
                    addView(container, layoutParams)
                }
            }
    }

}