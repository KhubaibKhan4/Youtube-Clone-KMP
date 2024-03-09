package org.company.app.di

import org.company.app.data.repository.Repository
import org.company.app.domain.repository.YoutubeApi
import org.company.app.presentation.MainViewModel
import org.koin.core.module.dsl.createdAtStart
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val  appModule = module {
    //single<YoutubeApi>(createdAtStart = true) { Repository() }
    singleOf(::Repository){
        createdAtStart()
    }
    factory {
        MainViewModel(repository = get())
    }
}