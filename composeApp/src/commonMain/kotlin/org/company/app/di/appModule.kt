package org.company.app.di

import YouTubeDatabase.db.YoutubeDatabase
import org.company.app.DriverFactory
import org.company.app.data.repository.Repository
import org.company.app.presentation.MainViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val appModule = module {
    single { Repository() }
    single { YoutubeDatabase(DriverFactory().createDriver()) }
    single {
        MainViewModel(repository = get(), database = get())
    }
}