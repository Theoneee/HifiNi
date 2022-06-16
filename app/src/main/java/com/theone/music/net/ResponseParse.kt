package com.theone.music.net

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

@Parser(name = "Response")
open class ResponseParse : TypeParser<Response>() {

    @Throws(IOException::class)
    override fun onParse(response: okhttp3.Response): Response {
        //第一步，解析code、msg字段，把data当成String对象
        val data: Response = GsonUtil.fromJson(response.body?.string(),Response::class.java)
        if (data.code == "0") {
            return data
        }
        throw ParseException(data.code, data.message, response)
    }

}