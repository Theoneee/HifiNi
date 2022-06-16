package com.theone.music.net

import com.theone.music.data.model.PgyerResponse
import com.theone.music.data.model.Response
import rxhttp.wrapper.annotation.Parser
import rxhttp.wrapper.exception.ParseException
import rxhttp.wrapper.parse.TypeParser
import rxhttp.wrapper.utils.GsonUtil
import rxhttp.wrapper.utils.convertTo
import java.io.IOException
import java.lang.reflect.Type


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
 * @date 2021/2/18 0018
 * @describe TODO
 * @email 625805189@qq.com
 * @remark
 */

@Parser(name = "PygerResponse")
open class PgyerResponseParse<T> : TypeParser<T> {

    protected constructor() : super()

    constructor(type: Type) : super(type)

    @Throws(IOException::class)
    override fun onParse(response: okhttp3.Response): T {
        //第一步，解析code、msg字段，把data当成String对象
        val data: PgyerResponse<String> = response.convertTo(Response::class, String::class.java)
        var t: T? = null
        if (data.code == 0) {
                //第二步，取出data字段，转换成我们想要的对象
                t = GsonUtil.getObject<T>(data.data, types[0])
        }
        if (t == null && types[0] === String::class.java) {
            //判断我们传入的泛型是String对象，就给t赋值""字符串，确保t不为null
            t = data.message as T
        }
        if (data.code != 0 || t == null) {
            throw ParseException(data.code.toString(), data.message, response)
        }
        return t
    }

}