package org.mxaln.compose.domain

import com.github.lamba92.kotlin.document.store.core.KotlinDocumentStore
import com.github.lamba92.kotlin.document.store.core.ObjectCollection
import com.github.lamba92.kotlin.document.store.core.find
import com.github.lamba92.kotlin.document.store.core.getObjectCollection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import org.mxaln.compose.data.Book

interface BookDataSource {
    suspend fun getAll(): ObjectCollection<Book>
    suspend fun getById(id: Long): Book?
    suspend fun getBySlug(slug: String): Book?
    suspend fun add(slug: String, name: String, content: String)
    suspend fun update(book: Book)
    suspend fun delete(id: Long)
}

class BookDataSourceImpl(private val db: KotlinDocumentStore) : BookDataSource {
    override suspend fun getAll(): ObjectCollection<Book> {
        return db.getObjectCollection<Book>("books")
    }

    override suspend fun getById(id: Long): Book? {
        return withContext(Dispatchers.Default) {
            getAll().findById(id)
        }
    }

    override suspend fun getBySlug(slug: String): Book? {
        return withContext(Dispatchers.Default) {
            getAll().find("slug", slug).firstOrNull()
        }
    }

    override suspend fun add(slug: String, name: String, content: String) {
        withContext(Dispatchers.Default) {
            val book = Book(
                slug = slug,
                name = name,
                content = content
            )
            getAll().insert(book)
        }
    }

    override suspend fun update(book: Book) {
        withContext(Dispatchers.Default) {
            getAll().insert(book)
        }
    }

    override suspend fun delete(id: Long) {
        withContext(Dispatchers.Default) {
            getAll().removeById(id)
        }
    }
}