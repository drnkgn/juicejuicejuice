package com.drnkgn.juicejuicejuice.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.drnkgn.juicejuicejuice.enums.TransactionType
import com.drnkgn.juicejuicejuice.states.TagUIState

@Entity(tableName = "tags")
data class Tag(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val type: TransactionType,
    @ColumnInfo(name = "deleted_at")
    val deletedAt: String? = null,
)

fun Tag.toUiState() = TagUIState(
    id = id,
    name = name,
    type = type,
    deletedAt = deletedAt
)