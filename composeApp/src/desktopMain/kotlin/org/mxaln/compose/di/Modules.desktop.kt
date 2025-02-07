package org.mxaln.compose.di

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.mxaln.compose.database.DB_NAME
import org.mxaln.database.MainDatabase
import java.io.File

actual val databaseModule = module {
    singleOf(::provideDatabaseDriver)
}

private fun provideDatabaseDriver(): SqlDriver {
    val dbFilePath = getPath()
    val driver = JdbcSqliteDriver("jdbc:sqlite:${dbFilePath}")

    if (!File(dbFilePath).exists()) {
        MainDatabase.Schema.create(driver)
    }

    return driver
}

private fun getPath(): String {
    val propertyKey = "user.home"
    val parentDirPath = "${System.getProperty(propertyKey)}/MaxProject"
    val parentDir = File(parentDirPath)
    if (!parentDir.exists()) {
        parentDir.mkdirs()
    }
    val database = File(parentDirPath, DB_NAME)
    return database.absolutePath
}