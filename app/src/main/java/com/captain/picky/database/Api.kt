package com.captain.picky.database

import com.captain.picky.models.ImagesResponseModel
import retrofit2.Call
import retrofit2.http.GET

interface Api {

    @GET(".")
    fun getAllImages(): Call<List<ImagesResponseModel>>
}