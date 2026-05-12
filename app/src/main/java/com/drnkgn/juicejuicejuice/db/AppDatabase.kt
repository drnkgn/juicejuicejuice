package com.drnkgn.juicejuicejuice.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.drnkgn.juicejuicejuice.db.converters.DateTimeConverters
import com.drnkgn.juicejuicejuice.db.converters.EnumConverters
import com.drnkgn.juicejuicejuice.db.dao.TagDAO
import com.drnkgn.juicejuicejuice.db.dao.TransactionDAO
import com.drnkgn.juicejuicejuice.db.dao.TransactionTagsDao
import com.drnkgn.juicejuicejuice.db.entities.Tag
import com.drnkgn.juicejuicejuice.db.entities.Transaction
import com.drnkgn.juicejuicejuice.db.entities.TransactionTagsPivot

@Database(
    entities = [
        Tag::class,
        Transaction::class,
        TransactionTagsPivot::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(DateTimeConverters::class, EnumConverters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun transactionTags(): TransactionTagsDao

    abstract fun transaction(): TransactionDAO

    abstract fun tag(): TagDAO
}