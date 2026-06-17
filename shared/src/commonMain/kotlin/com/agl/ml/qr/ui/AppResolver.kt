package com.agl.ml.qr.ui

import com.agl.ml.qr.model.AppInfo


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