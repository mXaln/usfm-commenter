package org.mxaln.compose

import app.cash.sqldelight.db.SqlDriver
import io.ktor.client.engine.HttpClientEngine

expect val httpClientEngine: HttpClientEngine
expect val appDirPath: String
expect val databaseDriver: SqlDriver