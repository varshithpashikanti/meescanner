package com.agl.ml.qr.ui

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.net.Uri
import com.agl.ml.qr.model.AppInfo
import java.io.ByteArrayOutputStream


actual class AppResolver(
    private val context: Context
) {

    actual fun getUpiApps(
        upiLink: String
    ): List<AppInfo> {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(upiLink))
        return queryApps(intent)
    }

    actual fun getUrlApps(
        url: String
    ): List<AppInfo> {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(url)
            addCategory(Intent.CATEGORY_BROWSABLE)
        }
        return queryApps(intent)
    }

    private fun queryApps(intent: Intent): List<AppInfo> {
        val packageManager = context.packageManager

        var resolveInfos = packageManager.queryIntentActivities(
            intent,
            PackageManager.MATCH_DEFAULT_ONLY
        )

        if (resolveInfos.isEmpty()) {
            resolveInfos = packageManager.queryIntentActivities(
                intent,
                0
            )
        }

        return resolveInfos.map {
            val icon = it.loadIcon(packageManager)
            AppInfo(
                appName = it.loadLabel(packageManager).toString(),
                appId   = it.activityInfo.packageName,
                appLogo = icon.toByteArray()
            )
        }.distinctBy { it.appId }
    }
}

private fun Drawable.toByteArray(): ByteArray? {
    return try {

        val width = if (intrinsicWidth > 0) intrinsicWidth else 144
        val height = if (intrinsicHeight > 0) intrinsicHeight else 144
        
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        setBounds(0, 0, canvas.width, canvas.height)
        draw(canvas)
        
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        stream.toByteArray()
    } catch (e: Exception) {
        null
    }
}
