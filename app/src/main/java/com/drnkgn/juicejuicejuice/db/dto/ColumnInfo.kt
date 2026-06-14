package com.drnkgn.juicejuicejuice.db.dto

data class ColumnInfo(
    val name: String,
    val type: String,
    val notNull: Boolean,
    val primaryKey: Boolean
)
