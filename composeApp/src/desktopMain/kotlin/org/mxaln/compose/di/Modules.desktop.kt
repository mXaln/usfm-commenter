package org.mxaln.compose.di

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.mxaln.compose.database.DB_NAME
import org.mxaln.compose.appDirPath
import org.mxaln.database.MainDatabase
import java.io.File

actual val databaseModule = module {
    singleOf(::provideDatabaseDriver)
}

private fun provideDatabaseDriver(): SqlDriver {
    val dbFile = getDatabaseFile()
    val driver = JdbcSqliteDriver("jdbc:sqlite:${dbFile.absolutePath}")

    if (!dbFile.exists()) {
        MainDatabase.Schema.create(driver)
    }

    return driver
}

fun getDatabaseFile(): File {
    val database = File(appDirPath, DB_NAME)
    return database
}