package org.company.app.di

import com.youtube.clone.db.YoutubeDatabase
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.company.app.DriverFactory
import org.company.app.data.remote.YoutubeClientApi
import org.company.app.data.repository.YouTubeServiceImpl
import org.company.app.presentation.viewmodel.MainViewModel
import org.company.app.utils.Constant
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val appModule = module {
    single {
        HttpClient {
            install(ContentNegotiation) {
                json(
                    Json {
                    isLenient = true
                    explicitNulls = false
                    ignoreUnknownKeys = true
                },
                    contentType = ContentType.Application.Json
                )
            }

            install(Logging) {
                level = LogLevel.ALL
                logger = object : Logger {
                    override fun log(message: String) {
                        println(message)
                    }

                }
            }

            install(HttpTimeout) {
                connectTimeoutMillis = Constant.TIMEOUT
                requestTimeoutMillis = Constant.TIMEOUT
                socketTimeoutMillis = Constant.TIMEOUT
            }
        }
    }
    single { YoutubeClientApi(get()) }
    singleOf(::YouTubeServiceImpl)
    single { YoutubeDatabase(DriverFactory().createDriver()) }
    singleOf(::MainViewModel)
}