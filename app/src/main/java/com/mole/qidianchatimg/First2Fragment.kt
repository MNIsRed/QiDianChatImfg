package com.mole.qidianchatimg

import android.content.ClipDescription.MIMETYPE_TEXT_PLAIN
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import coil.imageLoader
import coil.load
import coil.request.ImageRequest
import com.blankj.utilcode.util.ImageUtils
import com.mole.qidianchatimg.databinding.FragmentFirst2Binding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class First2Fragment : Fragment() {

    private var _binding: FragmentFirst2Binding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirst2Binding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //图片链接变化说明有可用图片下载
        viewModel.link.observe(viewLifecycleOwner) {
            binding.imageView.load(it)
            val request = ImageRequest.Builder(requireContext())
                .data(it).build()
            //为了能正常Toast，指定在main线程
            GlobalScope.launch(Dispatchers.Main) {
                //发现有的图片，coil无法加载，会返回null
                requireContext().imageLoader.execute(request).drawable?.let { targetDrawable ->
                    ImageUtils.save2Album(targetDrawable.toBitmap(), Bitmap.CompressFormat.JPEG)
                }?:let {
                    Toast.makeText(requireContext(), "未获取到图片", Toast.LENGTH_SHORT).show()
                }
            }
        }
        binding.confirmButton.setOnClickListener {
            val newUrl = binding.textviewFirst.text?.toString()
            if (!newUrl.isNullOrEmpty()) {
                viewModel.get(newUrl)
            } else {
                Toast.makeText(context, "无效链接", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val clipboard = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        //Android10及更高版本限制必须当前应用取得焦点后，clipboardManager才能获取到信息。
        binding.textviewFirst.postDelayed({
            when {
                !clipboard.hasPrimaryClip() -> {
                }
                !(clipboard.primaryClipDescription?.hasMimeType(MIMETYPE_TEXT_PLAIN)?:false) -> {
                }
                else -> {
                    val item = clipboard.primaryClip?.getItemAt(0)
                    val url = item?.text?.toString()
                    if (!viewModel.linkCopied(url)){
                        binding.textviewFirst.setText(url)
                        binding.confirmButton.performClick()
                    }
                }
            }
        },500)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}