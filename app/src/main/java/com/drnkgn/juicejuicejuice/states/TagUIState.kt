package com.drnkgn.juicejuicejuice.states

import com.drnkgn.juicejuicejuice.db.entities.Tag
import com.drnkgn.juicejuicejuice.enums.TransactionType

data class TagUIState(
    val id: Int,
    var name: String,
    var type: TransactionType,
    val deletedAt: String? = null
)

fun TagUIState.toEntity() = Tag(
    id = id,
    name = name,
    type = type,
    deletedAt = deletedAt
)