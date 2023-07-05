package com.mole.qidianchatimg

import android.Manifest
import android.content.ClipDescription.MIMETYPE_TEXT_PLAIN
import android.content.ClipboardManager
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.drawable.MovieDrawable
import coil.imageLoader
import coil.load
import coil.request.ImageRequest
import com.mole.qidianchatimg.databinding.FragmentFirst2Binding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import pl.droidsonroids.gif.GifDrawable
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException


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
        val coroutineScope = CoroutineScope(Dispatchers.Default)
        //图片链接变化说明有可用图片下载
        viewModel.link.observe(viewLifecycleOwner) {
            Log.d("TargetUrl","url is :$it")
            val isGif = it.contains(".gif")
            val request = ImageRequest.Builder(requireContext())
                .data(it).build()

            val imageLoader = ImageLoader.Builder(requireContext())
                .components {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        add(ImageDecoderDecoder.Factory())
                    } else {
                        add(GifDecoder.Factory())
                    }
                }
                .build()
            //为了能正常Toast，指定在main线程
            coroutineScope.launch{
                //发现有的图片，coil无法加载，会返回null
//                val imageResult = requireContext().imageLoader.execute(request)

                binding.imageView.load(it,imageLoader)
                val imageResult = imageLoader.execute(request)
                withContext(Dispatchers.Main){
                    imageResult.drawable?.let { targetDrawable ->
                        val fileName= if (isGif){
                            "${System.currentTimeMillis()}_img.gif"
                        }else{
                            "${System.currentTimeMillis()}_img.jpg"
                        }
                        if (hasQ){
                            val values = ContentValues()
                            values.put(
                                MediaStore.Images.Media.DISPLAY_NAME,
                                fileName
                            ) // 替换为您要保存的图片文件名
                            values.put(
                                MediaStore.Images.Media.MIME_TYPE,
                                "image/*"
                            ) // 替换为图片的MIME类型
                            val resolver: ContentResolver = requireContext().contentResolver
                            val contentUri = if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                            } else {
                                MediaStore.Images.Media.INTERNAL_CONTENT_URI
                            }

                            //设置文件所在位置
                            values.put(
                                MediaStore.Images.Media.RELATIVE_PATH,
                                Environment.DIRECTORY_PICTURES + "/" + "QiDianChatImg"
                            )

                            val imageUri = resolver.insert(
                                contentUri,
                                values
                            )
                            try {
                                val outputStream = resolver.openOutputStream(imageUri!!)
                                targetDrawable.toBitmap().compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                                // 执行写入操作到outputStream
                                outputStream!!.close()
                            } catch (e: IOException) {
                                e.printStackTrace()
                            }
                        }else{
                            //Android6.0以上，WRITE_EXTERNAL_STORAGE被视为危险权限，需要动态获取
                            if (hasM && !isGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                                Log.e("ImageUtils", "save to album need storage permission")
                                Toast.makeText(requireContext(), "无权限设置图片", Toast.LENGTH_SHORT).show()
                                ActivityCompat.requestPermissions(
                                    requireActivity(),
                                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                                    WRITE_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE
                                )
                                // 在 Android 6.0 及以上版本使用 ActivityCompat.requestPermissions()
                                ActivityCompat.requestPermissions(
                                    requireActivity(),
                                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                                    WRITE_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE
                                )
                                return@let
                            }
                            val picDir =
                                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                            val destFile = File(picDir, "/QiDianChatImg/$fileName")
                            if (isGif){
                                imageLoader.diskCache?.get(it)?.use {snapshot->
                                    val imageFile = snapshot.data.toFile()
                                    try {
                                        FileOutputStream(destFile).use { outputStream ->
                                            val fileInputStream = FileInputStream(imageFile)
                                            var buffer = ByteArray(1024)
                                            var byteRead = 0
                                            while (fileInputStream.read(buffer).also {
                                                    byteRead = it
                                                } != -1){
                                                outputStream.write(buffer,0,byteRead)
                                            }
                                            fileInputStream.close()
                                        }
                                    } catch (e: IOException) {
                                        e.printStackTrace()
                                    }
                                }

                            }else{
                                if (!save(targetDrawable.toBitmap(), destFile, Bitmap.CompressFormat.JPEG, 100, false)) {
                                    Toast.makeText(requireContext(), "无法保存图片", Toast.LENGTH_SHORT).show()
                                }
                            }

                            notifySystemToScan(destFile)
                        }
                    }?:let {
                        Toast.makeText(requireContext(), "未获取到图片", Toast.LENGTH_SHORT).show()
                    }
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == WRITE_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE){

        }
    }

    companion object{
        private const val WRITE_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE : Int = 123
    }
}