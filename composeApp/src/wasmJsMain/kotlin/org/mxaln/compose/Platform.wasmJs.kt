package org.mxaln.compose

import com.github.lamba92.kotlin.document.store.core.DataStore
import com.github.lamba92.kotlin.document.store.stores.browser.BrowserStore
import io.ktor.client.engine.js.Js

actual val httpClientEngine = Js.create()

actual val appDirPath: String
    get() = "/"

actual val dbStore: DataStore = BrowserStore
