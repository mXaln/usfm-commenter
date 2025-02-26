package org.mxaln.compose.domain

import com.github.lamba92.kotlin.document.store.core.KotlinDocumentStore
import com.github.lamba92.kotlin.document.store.core.ObjectCollection
import com.github.lamba92.kotlin.document.store.core.find
import com.github.lamba92.kotlin.document.store.core.getObjectCollection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.withContext
import org.mxaln.compose.data.Comment

interface CommentDataSource {
    suspend fun getAll(): ObjectCollection<Comment>
    suspend fun getByBook(bookId: Long): List<Comment>
    suspend fun getById(id: Long): Comment?
    suspend fun add(verse: Long, chapter: Long, comment: String, bookId: Long)
    suspend fun update(comment: Comment)
    suspend fun delete(id: Long)
}

class CommentDataSourceImpl(private val db: KotlinDocumentStore) : CommentDataSource {

    override suspend fun getAll(): ObjectCollection<Comment> {
        return db.getObjectCollection<Comment>("comments")
    }

    override suspend fun getByBook(bookId: Long): List<Comment> {
        return withContext(Dispatchers.Default) {
            getAll().find("bookId", bookId).toList()
        }
    }

    override suspend fun getById(id: Long): Comment? {
        return withContext(Dispatchers.Default) {
            getAll().findById(id)
        }
    }

    override suspend fun add(verse: Long, chapter: Long, comment: String, bookId: Long) {
        withContext(Dispatchers.Default) {
            val commentObj = Comment(
                verse = verse,
                chapter = chapter,
                comment = comment,
                bookId = bookId
            )
            getAll().insert(commentObj)
        }
    }

    override suspend fun update(comment: Comment) {
        withContext(Dispatchers.Default) {
            getAll().insert(comment)
        }
    }

    override suspend fun delete(id: Long) {
        withContext(Dispatchers.Default) {
            getAll().removeById(id)
        }
    }
}