package com.theone.music.data.model;//  ┏┓　　　┏┓
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

import com.theone.mvvm.core.data.net.IPageInfo;

/**
 * @author The one
 * @date 2022-04-12 13:18
 * @describe TODO
 * @email 625805189@qq.com
 * @remark
 */
public class PageInfo implements IPageInfo {

    private int page = 1;

    private int totalPage = 1;

    public PageInfo() {
    }

    public PageInfo(int page, int totalPage) {
        this.page = page;
        this.totalPage = totalPage;
    }

    @Override
    public int getPage() {
        return page;
    }

    @Override
    public int getPageSize() {
        return 0;
    }

    @Override
    public int getPageTotalCount() {
        return totalPage;
    }

    @Override
    public int getTotalCount() {
        return 0;
    }
}
