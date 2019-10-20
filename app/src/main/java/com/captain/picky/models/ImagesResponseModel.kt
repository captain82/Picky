package com.captain.picky.models

data class ImagesResponseModel(
    val width:Int?,
    val height:Int?,
    val id:String?,
    var author:String?,
    var download_url:String?,
    var url:String?,
    var visibility: Boolean = true
) {
}