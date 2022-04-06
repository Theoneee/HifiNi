package com.theone.music.viewmodel;//  ┏┓　　　┏┓
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

import com.theone.common.callback.databind.StringObservableField;
import com.theone.mvvm.base.viewmodel.BaseViewModel;

/**
 * @author The one
 * @date 2022-04-06 17:14
 * @describe TODO
 * @email 625805189@qq.com
 * @remark
 */
public class MineViewModel extends BaseViewModel {

    public final StringObservableField icon = new StringObservableField("https://www.hifini.com/view/img/logo.png");
    public final StringObservableField nickName = new StringObservableField("未登录");

}
