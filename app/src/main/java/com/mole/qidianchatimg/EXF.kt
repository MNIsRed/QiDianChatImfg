package com.mole.qidianchatimg

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.net.Uri
import android.os.Build
import android.util.Log
import android.util.Pair
import androidx.core.content.ContextCompat
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.Arrays

val hasQ:Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
val hasP:Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.P
val hasM:Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M

fun isGranted(vararg permissions: String): Boolean {
    val requestAndDeniedPermissions = getRequestAndDeniedPermissions(*permissions)

    val deniedPermissions = requestAndDeniedPermissions.second
    if (deniedPermissions.isNotEmpty()) {
        return false
    }
    val requestPermissions = requestAndDeniedPermissions.first
    for (permission in requestPermissions) {
        if (!isGranted(permission)) {
            return false
        }
    }
    return true
}

private fun isGranted(permission: String): Boolean {
    return (Build.VERSION.SDK_INT < Build.VERSION_CODES.M
            || PackageManager.PERMISSION_GRANTED
            == ContextCompat.checkSelfPermission(QiDianChatImgApplication.getInstance(), permission))
}

private fun getRequestAndDeniedPermissions(vararg permissionsParam: String): Pair<List<String>, List<String>> {
    val requestPermissions: MutableList<String> = ArrayList()
    val deniedPermissions: MutableList<String> = ArrayList()
    val appPermissions = getPermissions(QiDianChatImgApplication.getInstance().packageName)
    for (param in permissionsParam) {
        var isIncludeInManifest = false
        val permissions = getPermissions(param)
        for (permission in permissions) {
            if (appPermissions.contains(permission)) {
                requestPermissions.add(permission)
                isIncludeInManifest = true
            }
        }
        if (!isIncludeInManifest) {
            deniedPermissions.add(param)
            Log.e(
                "PermissionUtils",
                "U should add the permission of $param in manifest."
            )
        }
    }
    return Pair.create(requestPermissions, deniedPermissions)
}

fun getPermissions(packageName: String): List<String> {
    val pm = QiDianChatImgApplication.getInstance().packageManager
    return try {
        val permissions =
            pm.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS).requestedPermissions
                ?: return emptyList()
        Arrays.asList(*permissions)
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
        emptyList()
    }
}

fun save(
    src: Bitmap,
    file: File,
    format: CompressFormat?,
    quality: Int,
    recycle: Boolean
): Boolean {
    if (isEmptyBitmap(src)) {
        Log.e("ImageUtils", "bitmap is empty.")
        return false
    }
    if (src.isRecycled) {
        Log.e("ImageUtils", "bitmap is recycled.")
        return false
    }
    if (!createFileByDeleteOldFile(file)) {
        Log.e("ImageUtils", "create or delete file <$file> failed.")
        return false
    }
    var os: OutputStream? = null
    var ret = false
    try {
        os = BufferedOutputStream(FileOutputStream(file))
        ret = src.compress(format, quality, os)
        if (recycle && !src.isRecycled) src.recycle()
    } catch (e: IOException) {
        e.printStackTrace()
    } finally {
        try {
            os?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    return ret
}

private fun isEmptyBitmap(src: Bitmap?): Boolean {
    return src == null || src.width == 0 || src.height == 0
}

fun createFileByDeleteOldFile(file: File?): Boolean {
    if (file == null) return false
    // file exists and unsuccessfully delete then return false
    if (file.exists() && !file.delete()) return false
    return if (!createOrExistsDir(file.parentFile)) false else try {
        file.createNewFile()
    } catch (e: IOException) {
        e.printStackTrace()
        false
    }
}

fun createOrExistsDir(file: File?): Boolean {
    return file != null && if (file.exists()) file.isDirectory else file.mkdirs()
}

fun notifySystemToScan(file: File?) {
    if (file == null || !file.exists()) return
    val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
    intent.setData(Uri.parse("file://" + file.absolutePath))
    QiDianChatImgApplication.getInstance().sendBroadcast(intent)
}
