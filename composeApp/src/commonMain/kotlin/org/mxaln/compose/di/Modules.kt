package org.mxaln.compose.di

import com.github.lamba92.kotlin.document.store.core.KotlinDocumentStore
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import org.mxaln.compose.api.WacsApiClient
import org.mxaln.compose.data.Book
import org.mxaln.compose.dbStore
import org.mxaln.compose.domain.BookDataSource
import org.mxaln.compose.domain.BookDataSourceImpl
import org.mxaln.compose.domain.CommentDataSource
import org.mxaln.compose.domain.CommentDataSourceImpl
import org.mxaln.compose.domain.UsfmBookSource
import org.mxaln.compose.domain.UsfmBookSourceImpl
import org.mxaln.compose.httpClientEngine
import org.mxaln.compose.ui.BookViewModel
import org.mxaln.compose.ui.HomeViewModel

private val httpClient = HttpClient(httpClientEngine) {
    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true
            explicitNulls = true
        })
    }
}

val sharedModule = module {
    single { KotlinDocumentStore(dbStore) }

    // http clients
    singleOf(::httpClient)
    singleOf(::WacsApiClient)

    // data sources
    singleOf(::CommentDataSourceImpl).bind<CommentDataSource>()
    singleOf(::BookDataSourceImpl).bind<BookDataSource>()
    singleOf(::UsfmBookSourceImpl).bind<UsfmBookSource>()

    // view models
    factoryOf(::HomeViewModel)
    factory { (book: Book) -> BookViewModel(get(), get(), book = book) }
}
