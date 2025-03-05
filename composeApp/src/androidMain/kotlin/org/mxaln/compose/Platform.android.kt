package org.mxaln.compose

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import io.ktor.client.engine.android.Android
import org.koin.mp.KoinPlatform.getKoin
import org.mxaln.compose.database.DB_NAME
import org.mxaln.database.MainDatabase

actual val httpClientEngine
    get() = Android.create()
actual val appDirPath: String
    get() {
        val context: Context = getKoin().get()
        return context.getExternalFilesDir(null)?.canonicalPath
            ?: throw IllegalArgumentException("External files dir not found")
    }

actual val databaseDriver: SqlDriver
    get() {
        val context: Context = getKoin().get()
        return AndroidSqliteDriver(
            MainDatabase.Schema,
            context,
            DB_NAME
        )
    }