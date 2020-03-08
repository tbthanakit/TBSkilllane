package com.tb.myskilllane.service

import com.google.gson.Gson

class JsonMapperManager private constructor() {

    val gson = Gson()

    companion object {
        private val jsonMapperManager = JsonMapperManager()
        fun getInstance() = jsonMapperManager
    }
}