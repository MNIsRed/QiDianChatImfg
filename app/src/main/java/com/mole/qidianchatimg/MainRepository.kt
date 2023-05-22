package com.mole.qidianchatimg

import javax.inject.Inject

class MainRepository @Inject constructor(){
    suspend fun get(url: String): String {
        val api = retrofit.create(Api::class.java)
        return api.get(url)
    }

    suspend fun getJson(url: String): QiDianResponse {
        val api = retrofit.create(Api::class.java)
        return api.getJson(url)
    }
}