package org.mxaln.compose

import io.ktor.client.engine.cio.CIO

actual val httpClientEngine = CIO.create()
