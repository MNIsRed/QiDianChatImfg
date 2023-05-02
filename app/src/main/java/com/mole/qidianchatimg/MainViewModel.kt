package com.mole.qidianchatimg

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.util.regex.Pattern


class MainViewModel : ViewModel() {
    private val _link = MutableLiveData<String>()
    val link: LiveData<String> = _link

    fun get(url: String) {
        val pattern = Pattern.compile("https://h5v6\\.if\\.qidian\\.com/h5/share/chapterTalk\\?rootReviewId=(\\d*)")
        val matcher = pattern.matcher(url)
        if (!matcher.find()){
            return
        }
        val newUrl = "https://h5v6.if.qidian.com/argus/api/v1/chapterreview/getparagraphreviewshare?pg=1&rootReviewId=${matcher.group(1)}"
        val mainRepository = MainRepository()
        viewModelScope.launch {
            try {
                val result2 = mainRepository.getJson(newUrl)
                result2.Data.DataList.forEach {
                    if (it.ImageDetail.isNotEmpty()){
                        _link.value = it.ImageDetail
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

    }
}