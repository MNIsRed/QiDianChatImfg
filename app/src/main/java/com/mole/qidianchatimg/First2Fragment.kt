package com.mole.qidianchatimg

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
import kotlinx.coroutines.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
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
        viewModel.link.observe(viewLifecycleOwner) {
            binding.imageView.load(it)
            val request = ImageRequest.Builder(requireContext())
                .data(it).build()
            GlobalScope.launch {
                val result = requireContext().imageLoader.execute(request).drawable
                ImageUtils.save2Album(result!!.toBitmap(), Bitmap.CompressFormat.JPEG)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}