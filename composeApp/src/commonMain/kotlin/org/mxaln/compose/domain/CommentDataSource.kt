package org.mxaln.compose.domain

import app.cash.sqldelight.async.coroutines.awaitAsOneOrNull
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import org.mxaln.database.Comment
import org.mxaln.database.MainDatabase

interface CommentDataSource {
    fun getAll(): Flow<List<Comment>>
    suspend fun getByBook(bookId: Long): Flow<List<Comment>>
    suspend fun getById(id: Long): Comment?
    suspend fun add(verse: Long, chapter: Long, comment: String, bookId: Long)
    suspend fun update(comment: Comment)
    suspend fun delete(id: Long)
}

class CommentDataSourceImpl(db: MainDatabase) : CommentDataSource {
    private val queries = db.commentQueries

    override fun getAll(): Flow<List<Comment>> {
        return queries.getAll().asFlow().mapToList(Dispatchers.Default)
    }

    override suspend fun getByBook(bookId: Long): Flow<List<Comment>> {
        return withContext(Dispatchers.Default) {
            queries.getByBook(bookId).asFlow().mapToList(Dispatchers.Default)
        }
    }

    override suspend fun getById(id: Long): Comment? {
        return withContext(Dispatchers.Default) {
            queries.getById(id).awaitAsOneOrNull()
        }
    }

    override suspend fun add(verse: Long, chapter: Long, comment: String, bookId: Long) {
        withContext(Dispatchers.Default) {
            queries.add(verse, chapter, comment, bookId)
        }
    }

    override suspend fun update(comment: Comment) {
        withContext(Dispatchers.Default) {
            queries.update(
                id = comment.id,
                verse = comment.verse,
                chapter = comment.chapter,
                bookId = comment.bookId,
                comment = comment.comment,
                created = comment.created,
                modified = comment.modified
            )
        }
    }

    override suspend fun delete(id: Long) {
        withContext(Dispatchers.Default) {
            queries.delete(id)
        }
    }
}