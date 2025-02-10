package org.mxaln.compose.di

import io.ktor.client.HttpClient
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module
import org.mxaln.compose.domain.CommentDataSource
import org.mxaln.compose.domain.CommentDataSourceImpl
import org.mxaln.compose.domain.DirectoryProvider
import org.mxaln.compose.domain.DirectoryProviderImpl
import org.mxaln.compose.ui.HomeViewModel
import org.mxaln.compose.appDirPath
import org.mxaln.compose.domain.BookDataSource
import org.mxaln.compose.domain.BookDataSourceImpl
import org.mxaln.compose.domain.UsfmBookSource
import org.mxaln.compose.domain.UsfmBookSourceImpl
import org.mxaln.compose.httpClientEngine
import org.mxaln.compose.ui.BookViewModel
import org.mxaln.database.MainDatabase

expect val databaseModule: Module

val sharedModule = module {
    single { MainDatabase(get()) }
    single { HttpClient(httpClientEngine) {} }
    single { DirectoryProviderImpl(appDirPath) }.bind<DirectoryProvider>()
    singleOf(::CommentDataSourceImpl).bind<CommentDataSource>()
    singleOf(::BookDataSourceImpl).bind<BookDataSource>()
    singleOf(::UsfmBookSourceImpl).bind<UsfmBookSource>()
    viewModelOf(::HomeViewModel)
    viewModelOf(::BookViewModel)
}
