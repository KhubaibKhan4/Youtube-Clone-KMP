package org.company.app.di

import org.company.app.data.repository.Repository
import org.company.app.presentation.MainViewModel
import org.koin.dsl.module

val appModule = module {
    single { Repository() }
    single {
        MainViewModel(repository = get())
    }
}