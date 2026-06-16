package com.appgolive.meescanner.di


import com.agl.ml.history.repository.HistoryRepository
import com.agl.ml.history.repository.HistoryRepositoryImpl
import com.agl.ml.history.usecase.ScanHistoryUseCase
import com.agl.ml.history.viewmodel.HistoryViewModel
import com.appgolive.meescanner.document.DocumentViewModel
import com.appgolive.meescanner.home.viewmodel.HomeViewModel
import com.appgolive.meescanner.qr.viewmodel.QrViewModel
import com.appgolive.meescanner.result.viewmodel.ResultViewModel
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
