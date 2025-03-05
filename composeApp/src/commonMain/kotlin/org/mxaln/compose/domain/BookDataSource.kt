package org.mxaln.compose.domain

import app.cash.sqldelight.async.coroutines.awaitAsOneOrNull
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
    suspend fun add(slug: String, name: String, content: String)
    suspend fun update(book: Book)
    suspend fun delete(id: Long)
}

class BookDataSourceImpl(db: MainDatabase) : BookDataSource {
    private val queries = db.bookQueries

    override fun getAll(): Flow<List<Book>> {
        return queries.getAll().asFlow().mapToList(Dispatchers.Default)
    }

    override suspend fun getById(id: Long): Book? {
        return withContext(Dispatchers.Default) {
            queries.getById(id).awaitAsOneOrNull()
        }
    }

    override suspend fun getBySlug(slug: String): Book? {
        return withContext(Dispatchers.Default) {
            queries.getBySlug(slug).awaitAsOneOrNull()
        }
    }

    override suspend fun add(slug: String, name: String, content: String) {
        withContext(Dispatchers.Default) {
            queries.add(slug, name, content)
        }
    }

    override suspend fun update(book: Book) {
        withContext(Dispatchers.Default) {
            queries.update(
                id = book.id,
                slug = book.slug,
                name = book.name,
                content = book.content,
            )
        }
    }

    override suspend fun delete(id: Long) {
        withContext(Dispatchers.Default) {
            queries.delete(id)
        }
    }
}