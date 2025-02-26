package org.mxaln.compose.utils

import kotlin.math.floor
import kotlin.math.pow
import kotlin.random.Random

object Utils {
    fun randomString(length: Int): String {
        val charPool = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        return (1..length)
            .map { Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("")
    }

    fun bytesToHumanReadableSize(bytes: Double) = when {
        bytes >= 1 shl 30 -> format(bytes / (1 shl 30), "GB", 1)
        bytes >= 1 shl 20 -> format(bytes / (1 shl 20), "MB", 1)
        bytes >= 1 shl 10 -> format(bytes / (1 shl 10), "KB", 0)
        else -> "$bytes bytes"
    }

    private fun format(value: Double, unit: String, decimalPlaces: Int): String {
        val multiplier = 10.0.pow(decimalPlaces)
        val rounded = floor(value * multiplier) / multiplier
        return "$rounded $unit"
    }
}