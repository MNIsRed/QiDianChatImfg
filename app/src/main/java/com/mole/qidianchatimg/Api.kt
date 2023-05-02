package com.mole.qidianchatimg

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Url

interface Api {
    //使用suspend fun，协程就隐式调用了await，不需要声明为Call<String>
    @GET
    suspend fun get(@Url url: String): String
    @GET
    suspend fun getJson(@Url url: String):QiDianResponse
}