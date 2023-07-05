package com.mole.qidianchatimg

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class QiDianChatImgApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object{
        private lateinit var instance : QiDianChatImgApplication

        fun getInstance():QiDianChatImgApplication{
            return instance
        }
    }

}