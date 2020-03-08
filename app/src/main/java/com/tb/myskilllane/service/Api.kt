package com.tb.myskilllane.service

import com.tb.myskilllane.model.DataResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface Api {

    @GET("v2/list")
    fun getData(@Query("page") page: Int,
                @Query("limit") limit: Int): Call<Array<DataResponse>>

}