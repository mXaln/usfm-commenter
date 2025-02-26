package org.mxaln.compose

import com.github.lamba92.kotlin.document.store.core.DataStore
import io.ktor.client.engine.HttpClientEngine

expect val httpClientEngine: HttpClientEngine
expect val appDirPath: String
expect val dbStore: DataStore