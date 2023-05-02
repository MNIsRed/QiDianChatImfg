package com.mole.qidianchatimg

class MainRepository {
    suspend fun get(url: String): String {
        val api = retrofit.create(Api::class.java)
        return api.get(url)
    }

    suspend fun getJson(url: String): QiDianResponse {
        val api = retrofit.create(Api::class.java)
        return api.getJson(url)
    }
}