package com.theone.music.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class User(
    var email: String = "",
    var account: String = "",
    var password: String = "",
    var avatar: String = ""
) : Parcelable {

}