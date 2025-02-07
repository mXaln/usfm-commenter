package org.mxaln.compose.di

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.mxaln.compose.database.DB_NAME
import org.mxaln.database.MainDatabase

actual val databaseModule = module {
    singleOf(::provideDatabaseDriver)
}

private fun provideDatabaseDriver(context: Context): SqlDriver {
    return AndroidSqliteDriver(
        MainDatabase.Schema,
        context,
        DB_NAME
    )
}