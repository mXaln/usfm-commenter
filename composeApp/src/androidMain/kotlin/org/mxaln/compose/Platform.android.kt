package org.mxaln.compose

import io.ktor.client.engine.android.Android

actual val httpClientEngine = Android.create()
