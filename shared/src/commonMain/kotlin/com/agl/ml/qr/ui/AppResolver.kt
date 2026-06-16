package com.appgolive.meescanner.qr.ui

import com.appgolive.meescanner.qr.model.AppInfo


expect class AppResolver {

    fun getApps(
        link: String?
    )

    fun getUpiApps(
        upiLink: String
    ): List<AppInfo>

    fun getUrlApps(
        url: String
    ): List<AppInfo>

    fun getDocApp(
        link: String?
    )
}