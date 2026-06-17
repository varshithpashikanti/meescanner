package com.agl.ml.di


import com.agl.ml.history.repository.HistoryRepository
import com.agl.ml.history.repository.HistoryRepositoryImpl
import com.agl.ml.history.usecase.ScanHistoryUseCase
import com.agl.ml.history.viewmodel.HistoryViewModel
import com.agl.ml.document.DocumentViewModel
import com.agl.ml.home.viewmodel.HomeViewModel
import com.agl.ml.qr.viewmodel.QrViewModel
import com.agl.ml.result.viewmodel.ResultViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel {
        QrViewModel(
            appResolver = get()
        )
    }

    viewModel {
        ResultViewModel(
            qrViewModel = get()
        )
    }

    viewModel { HomeViewModel() }

    viewModel {
        DocumentViewModel(
            appResolver = get()
        )
    }

    viewModel {
        HistoryViewModel(
            get()
        )
    }

    single<HistoryRepository>{
        HistoryRepositoryImpl(
            dao = get()
        )
    }

    factory {
        ScanHistoryUseCase(
            repository = get()
        )
    }


}
