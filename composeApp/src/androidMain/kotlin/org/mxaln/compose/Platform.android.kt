package org.mxaln.compose

import android.content.Context
import com.github.lamba92.kotlin.document.store.core.DataStore
import com.github.lamba92.kotlin.document.store.stores.leveldb.android.openLevelDBStore
import io.ktor.client.engine.android.Android
import org.koin.mp.KoinPlatform.getKoin

actual val httpClientEngine = Android.create()
actual val appDirPath: String
    get() {
        val context: Context = getKoin().get()
        return context.getExternalFilesDir(null)?.canonicalPath
            ?: throw IllegalArgumentException("External files dir not found")
    }

actual val dbStore: DataStore
    get() {
        val context: Context = getKoin().get()
        return context.openLevelDBStore()
    }