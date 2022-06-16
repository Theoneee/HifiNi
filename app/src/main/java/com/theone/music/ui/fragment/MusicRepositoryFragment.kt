package com.theone.music.ui.fragment

import com.theone.music.viewmodel.MusicRepositoryViewModel
import com.zhpan.bannerview.BannerViewPager
import com.theone.music.data.model.Banner
import com.theone.music.ui.adapter.BannerAdapter
import com.zhpan.bannerview.constants.PageStyle
import com.qmuiteam.qmui.util.QMUIDisplayHelper
import androidx.recyclerview.widget.RecyclerView
import com.theone.music.R
import com.theone.music.ui.adapter.MusicActionAdapter
import com.theone.music.ui.fragment.rank.RankFragment
import com.theone.music.ui.fragment.signer.SingerFragment
import androidx.recyclerview.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import com.theone.music.data.model.Action
import com.theone.music.data.repository.DataRepository
import java.util.ArrayList

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
 * @date 2022-04-06 11:40
 * @describe TODO
 * @email 625805189@qq.com
 * @remark
 */
class MusicRepositoryFragment : BaseMusicFragment<MusicRepositoryViewModel>() {

    override fun isNeedChangeStatusBarMode(): Boolean {
        return true
    }

    override fun isStatusBarLightMode(): Boolean {
        return true
    }

    override fun showTopBar(): Boolean {
        return true
    }

    private fun initBannerView(banner: BannerViewPager<Banner>) {
        with(banner) {
            adapter = BannerAdapter()
            setLifecycleRegistry(lifecycle)
            setPageStyle(PageStyle.MULTI_PAGE_OVERLAP)
            setRevealWidth(QMUIDisplayHelper.dp2px(context, 20))
            setIndicatorVisibility(View.GONE)
            //setOnPageClickListener { _, _ -> }
            create(DataRepository.INSTANCE.getBannerList())
        }
    }

    private fun initActions(recyclerView: RecyclerView) {
        val actions = arrayListOf<Action>(
            Action(R.drawable.ic_action_hot, "热门"),
            Action(R.drawable.ic_action_rank, "排行"),
            Action(R.drawable.ic_action_singer, "歌手"),
            Action(R.drawable.ic_action_catogary, "分类")
        )
        with(recyclerView) {
            val musicActionAdapter = MusicActionAdapter().apply {
                setOnItemClickListener { adapter, _, position ->
                    val action = adapter.getItem(position) as Action
                    when (action.res) {
                        R.drawable.ic_action_hot -> startFragment(HotFragment())
                        R.drawable.ic_action_rank -> startFragment(RankFragment())
                        R.drawable.ic_action_singer -> startFragment(SingerFragment())
                        else -> startFragment(MainFragment())
                    }
                }
                setNewInstance(actions)
            }
            adapter = musicActionAdapter
            layoutManager = GridLayoutManager(context, 4)
        }
    }

    override fun initAdapter() {
        super.initAdapter()
        val topView =
            LayoutInflater.from(context).inflate(R.layout.custom_music_repository, null, false)
        getAdapter().addHeaderView(topView)
        val bannerViewPager: BannerViewPager<Banner> = topView.findViewById(R.id.banner_view)
        val actionRc: RecyclerView = topView.findViewById(R.id.action_recyclerView)
        initBannerView(bannerViewPager)
        initActions(actionRc)
    }
}