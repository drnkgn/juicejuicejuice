package com.drnkgn.juicejuicejuice.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.drnkgn.juicejuicejuice.enums.TransactionType
import com.drnkgn.juicejuicejuice.states.forms.TagForm

@Entity(tableName = "tags")
data class Tag(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val type: TransactionType,
    @ColumnInfo(name = "deleted_at")
    val deletedAt: String? = null,
)

fun Tag.toForm() = TagForm(
    id = id,
    name = name,
    type = type,
    deletedAt = deletedAt
)