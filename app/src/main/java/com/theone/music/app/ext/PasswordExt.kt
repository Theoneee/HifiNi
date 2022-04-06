package com.theone.music.app.ext

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
 * @date 2021-05-14 16:32
 * @describe TODO
 * @email 625805189@qq.com
 * @remark
 */

import android.util.Base64
import java.nio.charset.Charset
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

/**
 * 字符串加密工具
 *
 * @author D10NG
 * @date on 2019-06-27 10:10
 */

/** 加密key  */
private var PASSWORD_ENC_SECRET = "yyx-yx--2020-6-6"
/** 加密算法 */
private const val KEY_ALGORITHM = "AES"
/** 字符编码 */
private val CHARSET = Charset.forName("UTF-8")
/** 加解密算法/工作模式/填充方式 */
private const val CIPHER_ALGORITHM = "AES/CBC/PKCS7Padding"

/**
 * 对字符串加密
 * @param data  源字符串
 * @return  加密后的字符串
 */
fun String.encrypt(): String {
    val cipher = Cipher.getInstance(CIPHER_ALGORITHM)
    val byteArray = PASSWORD_ENC_SECRET.toByteArray(CHARSET)
    val keySpec = SecretKeySpec(byteArray, KEY_ALGORITHM)
    cipher.init(Cipher.ENCRYPT_MODE, keySpec, IvParameterSpec(byteArray))
    val encrypted = cipher.doFinal(this.toByteArray(CHARSET))
    return Base64.encodeToString(encrypted, Base64.NO_WRAP)
}

/**
 * 对字符串解密
 * @param data  已被加密的字符串
 * @return  解密得到的字符串
 */
fun String.decrypt(): String {
    val encrypted = Base64.decode(this.toByteArray(CHARSET), Base64.NO_WRAP)
    val cipher = Cipher.getInstance(CIPHER_ALGORITHM)
    val byteArray = PASSWORD_ENC_SECRET.toByteArray(CHARSET)
    val keySpec = SecretKeySpec(byteArray, KEY_ALGORITHM)
    cipher.init(Cipher.DECRYPT_MODE, keySpec, IvParameterSpec(byteArray))
    val original = cipher.doFinal(encrypted)
    return String(original, CHARSET)
}

