package com.mole.qidianchatimg

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.regex.Pattern
import javax.inject.Inject

/**
 * ViewModel中使用Hilt用@HiltViewModel代替@AndroidEntryPoint
 * 并且需要注入的对象，需要用@Inject constructor在构造器中声明
 */
@HiltViewModel
class MainViewModel @Inject constructor(private val mainRepository: MainRepository) : ViewModel() {
    private val _link = MutableLiveData<String>()
    val link: LiveData<String> = _link
    private var oldUrl: String = ""

//    @Inject
//    lateinit var

    /**
     * 通过复制链接，然后重新拼接成为查询接口
     * 解析返回json数据后获取图片链接
     */
    fun get(url: String) {
        val pattern =
            Pattern.compile("https://h5v6\\.if\\.qidian\\.com/h5/share/chapterTalk\\?rootReviewId=(\\d*)")
        val matcher = pattern.matcher(url)
        if (!matcher.find()) {
            return
        }
        oldUrl = url
        val newUrl =
            "https://h5v6.if.qidian.com/argus/api/v1/chapterreview/getparagraphreviewshare?pg=1&rootReviewId=${
                matcher.group(1)
            }"
        viewModelScope.launch {
            try {
                val result2 = mainRepository.getJson(newUrl)
                val data = result2.Data.DataList.first {
                    !it.ImageDetail.isNullOrEmpty()
                }
                _link.value = data.ImageDetail!!
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    /**
     * 避免因为自动查询剪贴板导致重复查询
     */
    fun linkCopied(url: String?): Boolean {
        return oldUrl == url
    }
}