package com.captain.picky.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.captain.picky.models.ImagesResponseModel
import com.captain.picky.repository.ImagesRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ImagesViewModel: ViewModel() {

    private val imagesRepository = ImagesRepository.getInstance()

    private val getAllImages: MutableLiveData<List<ImagesResponseModel>> = MutableLiveData()

    fun getAllImages(): LiveData<List<ImagesResponseModel>>
    {
        return getAllImages
    }

    fun getImagesFromNetwork() = imagesRepository.getAllImages().enqueue(object:
        Callback<List<ImagesResponseModel>> {
        override fun onFailure(call: Call<List<ImagesResponseModel>>, t: Throwable) {
            Log.i("Images" , t.localizedMessage)
        }

        override fun onResponse(
            call: Call<List<ImagesResponseModel>>, response: Response<List<ImagesResponseModel>>
        ) {
            if (response.isSuccessful)
            {
                Log.i("Images" , response.body()?.size.toString())
                getAllImages.postValue(response.body())
            }
            else
            {
                Log.i("Images" , response.body()?.size.toString())
                getAllImages.postValue(null)
            }
        }
    })




}