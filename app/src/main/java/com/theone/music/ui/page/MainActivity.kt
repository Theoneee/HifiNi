package com.theone.music.ui.page

import android.os.Bundle
import com.hjq.permissions.OnPermission
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.qmuiteam.qmui.arch.annotation.DefaultFirstFragment
import com.theone.mvvm.base.activity.BaseFragmentActivity

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
 * @date 2022-01-04 11:24
 * @describe TODO
 * @email 625805189@qq.com
 * @remark
 */
@DefaultFirstFragment(MainFragment::class)
class MainActivity:BaseFragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermission()
    }

    /**
     * 请求权限
     */
    private fun requestPermission() {
        XXPermissions.with(this)
            .permission(Permission.MANAGE_EXTERNAL_STORAGE)
            .constantRequest()
            .request(object : OnPermission {

                override fun hasPermission(granted: MutableList<String>?, all: Boolean) {

                }

                override fun noPermission(denied: MutableList<String>?, quick: Boolean) {
                }
            })
    }

}