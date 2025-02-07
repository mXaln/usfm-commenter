package org.mxaln.compose.dependencies

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import org.mxaln.database.Comment
import org.mxaln.database.MainDatabase

interface CommentDataSource {
    fun getAllComments(): Flow<List<Comment>>
    suspend fun getComment(verse: Long, chapter: Long, book: Long): Comment?
    suspend fun getComment(id: Long): Comment?
    suspend fun saveComment(comment: Comment)
    suspend fun deleteComment(id: Long)
}

class CommentDataSourceImpl(db: MainDatabase) : CommentDataSource {
    private val queries = db.commentQueries

    override fun getAllComments(): Flow<List<Comment>> {
        return queries.getAllComments().asFlow().mapToList(Dispatchers.IO)
    }

    override suspend fun getComment(verse: Long, chapter: Long, book: Long): Comment? {
        return withContext(Dispatchers.IO) {
            queries.getComment(verse, chapter, book).executeAsOneOrNull()
        }
    }

    override suspend fun getComment(id: Long): Comment? {
        return withContext(Dispatchers.IO) {
            queries.getCommentById(id).executeAsOneOrNull()
        }
    }

    override suspend fun saveComment(comment: Comment) {
        withContext(Dispatchers.IO) {
            queries.saveComment(
                id = comment.id,
                verse = comment.verse,
                chapter = comment.chapter,
                book = comment.book,
                comment = comment.comment,
                created = comment.created,
                modified = comment.modified
            )
        }
    }

    override suspend fun deleteComment(id: Long) {
        withContext(Dispatchers.IO) {
            queries.deleteComment(id)
        }
    }
}