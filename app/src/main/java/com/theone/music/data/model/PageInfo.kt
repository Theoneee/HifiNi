package com.theone.music.data.model

import com.theone.mvvm.core.data.net.IPageInfo

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
 * @date 2022-04-07 08:55
 * @describe TODO
 * @email 625805189@qq.com
 * @remark
 */
class PageInfo(private val curPage:Int = 1,private val totalPage:Int = 1):IPageInfo {

    override fun getPage(): Int = curPage

    override fun getPageSize(): Int = 1

    override fun getPageTotalCount(): Int = totalPage

    override fun getTotalCount(): Int  = 1
}