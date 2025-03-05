package org.mxaln.compose

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import io.ktor.client.engine.cio.CIO
import org.mxaln.compose.database.DB_NAME
import org.mxaln.database.MainDatabase
import java.io.File

actual val httpClientEngine
    get() = CIO.create()
actual val appDirPath: String
    get() {
        val propertyKey = "user.home"
        val appDirPath = "${System.getProperty(propertyKey)}/UsfmCommenter"
        val appDir = File(appDirPath)
        if (!appDir.exists()) {
            appDir.mkdirs()
        }
        return appDir.canonicalPath
    }
actual val databaseDriver: SqlDriver
    get() {
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