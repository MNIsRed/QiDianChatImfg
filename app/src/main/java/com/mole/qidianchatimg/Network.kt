package com.mole.qidianchatimg

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

//支持kotlin需要MoshiConverterFactory创建时添加配置。
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

val retrofit = Retrofit.Builder()
    .baseUrl("https://localhost/")//domain必须带这个"/"符号，interface中接口地址不以"/"开头
    .addConverterFactory(CustomConvertFactory())
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .build()