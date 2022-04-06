package com.theone.music.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize


@Entity(tableName = "user", indices = [Index(value = ["account"], unique = true)])
@Parcelize
data class User(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var account: String = "",
    var nickname: String? = null,
    var password: String = "",
    var sex: Int = 0
) : Parcelable {

    @Ignore
    constructor() : this(0) {}
}