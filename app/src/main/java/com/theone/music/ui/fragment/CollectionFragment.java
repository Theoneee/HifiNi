package com.theone.music.ui.fragment;//  ┏┓　　　┏┓
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

import androidx.lifecycle.Observer;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.theone.music.R;
import com.theone.music.app.ext.AppExtKt;
import com.theone.music.data.model.CollectionEvent;
import com.theone.music.data.model.Music;
import com.theone.music.data.model.User;
import com.theone.music.viewmodel.CollectionViewModel;
import com.theone.mvvm.core.app.ext.LoaderExtKt;

import java.util.List;

/**
 * @author The one
 * @date 2022-04-11 10:44
 * @describe 收藏
 * @email 625805189@qq.com
 * @remark
 */
public class CollectionFragment extends BaseMusicFragment<CollectionViewModel> {

    @Override
    protected String getTopBarTitle() {
        return "收藏";
    }

    @Override
    public void initData() {
        super.initData();
        User user = getEventVm().getUserInfoLiveData().getValue();
        if (null != user) {
            getViewModel().setUserId(user.id);
        }
    }

    @Override
    public void setRefreshLayoutEnabled(boolean enabled) {
        super.setRefreshLayoutEnabled(false);
    }

    @Override
    public void createObserver() {
        super.createObserver();
        getEventVm().getCollectionLiveData().observe(this, new Observer<CollectionEvent>() {
            @Override
            public void onChanged(CollectionEvent event) {
                // 是收藏，刷新
                if (event.isCollection()) {
                    onAutoRefresh();
                } else {
                    Music collection = event.getMusic();
                    // 取消收藏
                    BaseQuickAdapter<Music, ?> adapter = getMAdapter();
                    List<Music> datas = adapter.getData();
                    // 如果只有一条数据，直接显示空界面
                    if (datas.size() == 1) {
                        LoaderExtKt.showEmptyPage(CollectionFragment.this, "暂无此内容", R.drawable.status_search_result_empty, null);
                    } else {
                        // 遍历
                        for (int i = 0; i < datas.size(); i++) {
                            Music music = datas.get(i);
                            // 找到相同的那个
                            if (music.shareUrl.equals(collection.shareUrl)) {
                                // 移除
                                AppExtKt.removeItem(adapter,i);
                                break;
                            }
                        }
                    }
                }
            }
        });

        // 播放地址失效后，再次请求成功后，更新数据
        getEventVm().getReloadMusicLiveData().observe(this, new Observer<Music>() {
            @Override
            public void onChanged(Music changed) {
                BaseQuickAdapter<Music, ?> adapter = getMAdapter();
                List<Music> datas = adapter.getData();
                for (int i = 0; i < datas.size(); i++) {
                    Music music = datas.get(i);
                    if (music.shareUrl.equals(changed.shareUrl)) {
                        music.url = changed.url;
                        music.realUrl = changed.realUrl;
                        break;
                    }
                }
            }
        });
    }
}
