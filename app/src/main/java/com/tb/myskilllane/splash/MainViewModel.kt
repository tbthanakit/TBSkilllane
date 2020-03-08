package com.tb.myskilllane.splash

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tb.myskilllane.database.RealmManager
import com.tb.myskilllane.database.entity.DataEntity
import com.tb.myskilllane.model.DataResponse
import com.tb.myskilllane.service.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {

    val whenDataLoaded = MutableLiveData<Array<DataResponse>>()
    val whenDataFailure = MutableLiveData<Boolean>()

    fun getData() {
        ApiService.getInstance().getData(
            page = 1, limit = 3
        ).enqueue(object : Callback<Array<DataResponse>> {
            override fun onFailure(call: Call<Array<DataResponse>>, t: Throwable) {
                whenDataFailure.postValue(true)
            }

            override fun onResponse(
                call: Call<Array<DataResponse>>,
                response: Response<Array<DataResponse>>
            ) {
                if (response.isSuccessful) {
                    if (RealmManager.getInstance().getDataList().isNullOrEmpty()) {
                        RealmManager.getInstance().save(DataEntity::class.java, response.body()!!.map {
                            DataEntity(
                                id = it.id,
                                author = it.author,
                                downloadUrl = it.downloadUrl
                            )
                        })
                    } else {
                        response.body()!!.forEach { data ->
                            if (RealmManager.getInstance().getDataList().filter {
                                it.id == data.id && it.author == data.author
                            }.isNullOrEmpty()) {
                                RealmManager.getInstance().saveData(DataEntity().apply {
                                    this.id = data.id
                                    this.author = data.author
                                    this.downloadUrl = data.downloadUrl
                                })
                            }
                        }
                    }
                    whenDataLoaded.postValue(response.body())
                } else {
                    whenDataFailure.postValue(true)
                }
            }

        })
    }
}