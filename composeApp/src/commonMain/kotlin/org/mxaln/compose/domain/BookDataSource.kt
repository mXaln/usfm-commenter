package org.mxaln.compose.domain

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import org.mxaln.database.Book
import org.mxaln.database.MainDatabase

interface BookDataSource {
    fun getAll(): Flow<List<Book>>
    suspend fun getById(id: Long): Book?
    suspend fun getBySlug(slug: String): Book?
    suspend fun add(slug: String, name: String, path: String)
    suspend fun update(book: Book)
    suspend fun delete(id: Long)
}

class BookDataSourceImpl(db: MainDatabase) : BookDataSource {
    private val queries = db.bookQueries

    override fun getAll(): Flow<List<Book>> {
        return queries.getAll().asFlow().mapToList(Dispatchers.IO)
    }

    override suspend fun getById(id: Long): Book? {
        return withContext(Dispatchers.IO) {
            queries.getById(id).executeAsOneOrNull()
        }
    }

    override suspend fun getBySlug(slug: String): Book? {
        return withContext(Dispatchers.IO) {
            queries.getBySlug(slug).executeAsOneOrNull()
        }
    }

    override suspend fun add(slug: String, name: String, path: String) {
        withContext(Dispatchers.IO) {
            queries.add(slug, name, path)
        }
    }

    override suspend fun update(book: Book) {
        withContext(Dispatchers.IO) {
            queries.update(
                id = book.id,
                slug = book.slug,
                name = book.name,
                document = book.document,
            )
        }
    }

    override suspend fun delete(id: Long) {
        withContext(Dispatchers.IO) {
            queries.delete(id)
        }
    }
}