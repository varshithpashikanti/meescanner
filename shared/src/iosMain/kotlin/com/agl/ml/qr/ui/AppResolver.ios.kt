package com.appgolive.meescanner.qr.ui

import com.appgolive.meescanner.qr.model.AppInfo

actual class AppResolver {
    actual fun getApps(link: String?) {
    }

    actual fun getUpiApps(upiLink: String): List<AppInfo> {
        TODO("Not yet implemented")
    }

    actual fun getUrlApps(url: String): List<AppInfo> {
        TODO("Not yet implemented")
    }

    actual fun getDocApp(link: String?) {
    }
}