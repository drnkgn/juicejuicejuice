package com.drnkgn.juicejuicejuice.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.drnkgn.juicejuicejuice.db.entities.Tag

@Dao
interface TagDAO {
    @Query("SELECT * FROM tags ORDER BY CASE WHEN deleted_at IS NULL THEN 0 ELSE 1 END")
    suspend fun getAllTagsWithTrashed(): List<Tag>

    @Query("SELECT * FROM tags WHERE deleted_at IS NULL")
    suspend fun getAllTags(): List<Tag>

    @Query("SELECT * FROM tags WHERE id = :id")
    suspend fun findById(id: Int): Tag?

    @Insert
    suspend fun create(tag: Tag)

    @Update
    suspend fun update(tag: Tag)
}