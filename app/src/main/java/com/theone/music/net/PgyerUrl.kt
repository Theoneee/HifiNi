package com.theone.music.net

import rxhttp.wrapper.annotation.Domain


/**
 * @author The one
 * @date 2022-06-16 22:10
 * @describe 蒲公英相关API
 * @email 625805189@qq.com
 * @remark
 */
object PgyerUrl {

    const val API_KEY = "016fdb173278e52b3be8b605418aacbb"
    const val APP_KEY = "b6dc24478f1aab9ded6794fa0256f6d9"

    @Domain(name = "Pgyer")
    const val BASE_URL = "https://www.pgyer.com/apiv2/"

    const val CHECK_UPDATE = "app/check"
}