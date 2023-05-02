package com.mole.qidianchatimg

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val gson = GsonBuilder().setLenient().create()
val retrofit = Retrofit.Builder()
    .baseUrl("https://localhost/")//domain必须带这个"/"符号，interface中接口地址不以"/"开头
    .addConverterFactory(CustomConvertFactory())
    .addConverterFactory(GsonConverterFactory.create(gson))
    .build()