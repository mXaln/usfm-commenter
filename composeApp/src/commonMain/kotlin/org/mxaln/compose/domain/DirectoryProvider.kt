package org.mxaln.compose.domain

import dev.zwander.kotlin.file.IPlatformFile
import dev.zwander.kotlin.file.PlatformFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.io.readString
import org.mxaln.compose.utils.Utils

interface DirectoryProvider {
    val rootDir: IPlatformFile
    val documentsDir: IPlatformFile
    val tempDir: IPlatformFile

    suspend fun saveDocument(file: IPlatformFile): IPlatformFile

    suspend fun readDocument(fileName: String): String?
    suspend fun readDocument(file: IPlatformFile): String?

    suspend fun deleteDocument(fileName: String)

    suspend fun createTempFile(prefix: String, suffix: String): IPlatformFile
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

    override val tempDir: IPlatformFile
        get() {
            val dir = rootDir.child("temp", true)
            return dir?.let {
                if (!it.getExists()) {
                    it.mkdir()
                }
                it
            } ?: throw IllegalArgumentException("Temp directory not found")
        }

    override suspend fun saveDocument(file: IPlatformFile): IPlatformFile {
        return withContext(Dispatchers.IO) {
            val fileName = Utils.randomString(10)
            documentsDir.child(fileName, false)?.let { target ->
                file.openInputStream()?.use { input ->
                    target.openOutputStream()?.use { output ->
                        input.readTo(output, file.getLength())
                    }
                }
                target
            }
        } ?: throw IllegalArgumentException("Could not save document")
    }

    override suspend fun readDocument(fileName: String): String? {
        return documentsDir.child(fileName, false)?.let {
            readDocument(it)
        }
    }

    override suspend fun readDocument(file: IPlatformFile): String? {
        return try {
            if (file.getExists()) {
                file.openInputStream()?.use { stream ->
                    stream.readString()
                }
            } else null
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun deleteDocument(fileName: String) {
        documentsDir.child(fileName, false)?.delete()
    }

    override suspend fun createTempFile(prefix: String, suffix: String): IPlatformFile {
        val name = prefix + Utils.randomString(8) + suffix
        val file = tempDir.child(name, false)
        val created = file?.createNewFile() ?: false
        return if (created) {
            file!!
        } else throw IllegalArgumentException("Could not create temp file.")
    }
}