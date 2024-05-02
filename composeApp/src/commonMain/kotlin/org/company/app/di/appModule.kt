package org.company.app.di

import com.youtube.clone.db.YoutubeDatabase
import org.company.app.DriverFactory
import org.company.app.data.repository.YouTubeServiceImpl
import org.company.app.presentation.viewmodel.MainViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val appModule = module {
    single{ YouTubeServiceImpl() }
    single { YoutubeDatabase(DriverFactory().createDriver()) }
    singleOf(::MainViewModel)
}