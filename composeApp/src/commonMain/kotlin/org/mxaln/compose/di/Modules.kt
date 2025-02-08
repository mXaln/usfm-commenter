package org.mxaln.compose.di

import io.ktor.client.HttpClient
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module
import org.mxaln.compose.dependencies.CommentDataSource
import org.mxaln.compose.dependencies.CommentDataSourceImpl
import org.mxaln.compose.dependencies.DirectoryProvider
import org.mxaln.compose.dependencies.DirectoryProviderImpl
import org.mxaln.compose.dependencies.MyViewModel
import org.mxaln.compose.appDirPath
import org.mxaln.compose.httpClientEngine
import org.mxaln.database.MainDatabase

expect val databaseModule: Module

val sharedModule = module {
    single { MainDatabase(get()) }
    single { HttpClient(httpClientEngine) {} }
    single { DirectoryProviderImpl(appDirPath) }.bind<DirectoryProvider>()
    singleOf(::CommentDataSourceImpl).bind<CommentDataSource>()
    viewModelOf(::MyViewModel)
}
