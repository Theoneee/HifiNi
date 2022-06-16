package com.theone.music.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.theone.common.ext.getVersionCode
import com.theone.mvvm.base.appContext
import com.theone.mvvm.core.base.callback.IApkUpdate
import kotlinx.parcelize.Parcelize


//{
//        "buildBuildVersion":"3",
//        "forceUpdateVersion":"",
//        "forceUpdateVersionNo":"",
//        "needForceUpdate":false,
//        "downloadURL":"https:\/\/www.pgyer.com\/app\/installUpdate\/06b30bd60a2b0ef6438787435aea1a8b?sig=VM2rutdZfikem3JiY247T4Z4y2GCC4igRJju4r1JNpub687ZQoEE2pqof8fDi3tc&amp;forceHttps=",
//        "buildHaveNewVersion":false,
//        "buildVersionNo":"11",
//        "buildVersion":"1.3.0",
//        "buildUpdateDescription":"\u5f15\u5165\u84b2\u516c\u82f1",
//        "appKey":"9bd0a24ff6feb1ccc84cf49b6b90521a",
//        "buildKey":"06b30bd60a2b0ef6438787435aea1a8b",
//        "buildName":"HifiNi",
//        "buildIcon":"https:\/\/cdn-app-icon.pgyer.com\/c\/3\/5\/5\/7\/c35571bc7a0bc95f2ae7745938124f56?x-oss-process=image\/resize,m_lfit,h_120,w_120\/format,jpg",
//        "buildFileKey":"57c5f364859b9f8e6f5306cd1a52f1b0.apk",
//        "buildFileSize":"9895573"
//    }
@Parcelize
data class Update(
    @SerializedName("buildVersionNo")
    val code:Int,
    @SerializedName("buildVersion")
    val versionName:String,
    @SerializedName("buildUpdateDescription")
    val des:String,
    @SerializedName("downloadURL")
    val url:String,
    @SerializedName("buildHaveNewVersion")
    val new:Boolean,
    @SerializedName("buildFileSize")
    val size:Long,
    ) :IApkUpdate,Parcelable {

    override fun getAppApkSize(): Long  = size

    override fun getAppApkUrl(): String  = url

    override fun getAppUpdateLog(): String = des

    override fun getAppVersionCode(): Int = code

    override fun getAppVersionName(): String = versionName

    override fun isForceUpdate(): Boolean  = false

    override fun isNewVersion(): Boolean = code > appContext.getVersionCode()

}