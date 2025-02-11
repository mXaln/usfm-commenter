package org.mxaln.compose

import io.ktor.client.engine.cio.CIO
import java.io.File

actual val httpClientEngine = CIO.create()
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