package com.appgolive.meescanner.qr.ui

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.Toast
import androidx.core.content.FileProvider
import com.appgolive.meescanner.qr.model.AppInfo
import java.io.ByteArrayOutputStream
import java.io.File


actual class AppResolver(
    private val context: Context
) {


    actual fun getUpiApps(
        upiLink: String
    ): List<AppInfo> {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(upiLink))
        val chooser = Intent.createChooser(intent, "Pay with")
        chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(chooser)
        return queryApps(intent)
    }

    actual fun getUrlApps(
        url: String
    ): List<AppInfo> {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(url)
            addCategory(Intent.CATEGORY_BROWSABLE)
        }
        val chooser = Intent.createChooser(intent, "Pay with")
        chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(chooser)
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

    actual fun getApps(
        link: String?
    ) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
//        val chooser = Intent.createChooser(intent, "Open with")
        val chooser = Intent.createChooser(intent, "Pay with")
        chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(chooser)
    }

    actual fun getDocApp(
        link: String?
    ) {
        if (link == null) return

        val uri = convertToContentUri(link)

        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/pdf")
            // IMPORTANT: grant read permission to the receiving app
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        // Safe start — don't crash if no PDF app installed
        try {
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            // No PDF viewer installed — handle gracefully
            Toast.makeText(context, "No PDF viewer found", Toast.LENGTH_SHORT).show()
        }
    }

    private fun convertToContentUri(uriString: String): Uri {
        val uri = Uri.parse(uriString)

        // Already a content:// URI? Use as-is
        if (uri.scheme == "content") return uri

        // It's a file:// URI — convert it
        val file = File(uri.path!!)
        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",  // must match manifest
            file
        )
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
