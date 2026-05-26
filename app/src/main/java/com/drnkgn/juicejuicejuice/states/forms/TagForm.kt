package com.drnkgn.juicejuicejuice.states.forms

import com.drnkgn.juicejuicejuice.db.entities.Tag
import com.drnkgn.juicejuicejuice.enums.TransactionType
import java.time.LocalDateTime

data class TagForm(
    val id: Int,
    var name: String,
    var type: TransactionType,
    val deletedAt: LocalDateTime? = null
)

fun TagForm.toEntity() = Tag(
    id = id,
    name = name,
    type = type,
    deletedAt = deletedAt
)