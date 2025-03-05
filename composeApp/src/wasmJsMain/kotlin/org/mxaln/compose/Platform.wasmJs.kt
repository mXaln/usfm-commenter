package org.mxaln.compose

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.worker.WebWorkerDriver
import io.ktor.client.engine.js.Js
import org.w3c.dom.Worker

actual val httpClientEngine
    get() = Js.create()

actual val appDirPath: String
    get() = "/"

actual val databaseDriver: SqlDriver
    get() {
        return WebWorkerDriver(jsWorker())
    }

internal fun jsWorker(): Worker =
    js("""new Worker(new URL("./sqlite.worker.mjs", import.meta.url))""")