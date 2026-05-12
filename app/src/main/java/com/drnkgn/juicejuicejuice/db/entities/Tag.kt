package com.drnkgn.juicejuicejuice.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.drnkgn.juicejuicejuice.enums.TransactionType

@Entity(tableName = "tags")
data class Tag(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val type: TransactionType
)