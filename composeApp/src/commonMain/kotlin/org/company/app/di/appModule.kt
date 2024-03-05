package org.company.app.di

import org.company.app.data.repository.Repository
import org.koin.dsl.module

fun appModule() = module {
    single<Repository> { Repository() }
}