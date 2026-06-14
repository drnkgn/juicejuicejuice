package com.drnkgn.juicejuicejuice.db

object DBSchema {
    val schema = mapOf(
        "tags" to listOf(
            "id",
            "name",
            "type",
            "deleted_at",
        ),
        "transactions" to listOf(
            "id",
            "type",
            "amount",
            "transaction_at",
            "description",
            "created_at",
            "deleted_at"
        ),
        "transaction_tags" to listOf(
            "transaction_id",
            "tag_id"
        )
    )
}