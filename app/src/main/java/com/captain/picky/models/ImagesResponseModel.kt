package com.captain.picky.models

/*data class ImagesResponseModel(val format:String?,
                               val width:Int?,
                               val height:Int?,
                               val filename:String?,
                               val id:Int?,
                               val author:String?,
                               val author_url:String?,
                               val post_url:String?
) {
}*/

data class ImagesResponseModel(
    val width:Int?,
    val height:Int?,
    val id:String?,
    var author:String?,
    var download_url:String?,
    var url:String?,
    var visisbility: Boolean = true
) {
}