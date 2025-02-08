package org.mxaln.compose.dependencies

import dev.zwander.kotlin.file.IPlatformFile
import dev.zwander.kotlin.file.PlatformFile
import kotlinx.io.asOutputStream
import kotlinx.io.readString

interface DirectoryProvider {
    val rootDir: IPlatformFile
    val documentsDir: IPlatformFile

    suspend fun saveDocument(file: IPlatformFile)
    suspend fun saveDocument(bytes: ByteArray, fileName: String)

    suspend fun readDocument(fileName: String): String?
}

class DirectoryProviderImpl(appDirPath: String) : DirectoryProvider {

    override val rootDir = PlatformFile(appDirPath)
    override val documentsDir: IPlatformFile
        get() {
            val dir = rootDir.child("documents", true)
            return dir?.let {
                if (!it.getExists()) {
                    it.mkdir()
                }
                it
            } ?: throw IllegalArgumentException("Documents directory not found")
        }

    override suspend fun saveDocument(file: IPlatformFile) {
        documentsDir.child(file.getName(), false)?.let { target ->
            file.openInputStream()?.use { input ->
                target.openOutputStream()?.use { output ->
                    input.readTo(output, file.getLength())
                }
            }
        }
    }

    override suspend fun saveDocument(bytes: ByteArray, fileName: String) {
        documentsDir.child(fileName, false)?.let { target ->
            bytes.inputStream().use { input ->
                target.openOutputStream()?.use { output ->
                    input.copyTo(output.asOutputStream(), 1024)
                }
            }
        }
    }

    override suspend fun readDocument(fileName: String): String? {
        return try {
            documentsDir.child(fileName, false)?.let { file ->
                if (file.getExists()) {
                    file.openInputStream()?.use { stream ->
                        stream.readString()
                    }
                } else null
            }
        } catch (e: Exception) {
            null
        }
    }
}