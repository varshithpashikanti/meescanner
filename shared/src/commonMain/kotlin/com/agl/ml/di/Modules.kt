package com.agl.ml.di

import com.agl.ml.home.viewmodel.HomeViewModel
import com.agl.ml.qr.viewmodel.QrViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel {
        QrViewModel(
            appResolver = get()
        )
    }
    viewModel { HomeViewModel() }
}
