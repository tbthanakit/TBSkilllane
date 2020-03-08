package com.tb.myskilllane.service

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiService private constructor() {

    companion object {

        private val httpClient = OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)

        fun getInstance(): Api {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://picsum.photos/")
                .client(httpClient.build())
                .build()

            return retrofit.create(Api::class.java)
        }
    }

}