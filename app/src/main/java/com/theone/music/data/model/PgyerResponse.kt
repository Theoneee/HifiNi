package com.theone.music.data.model

data class PgyerResponse<T>(val code:Int,val message:String,val data:T) {
}