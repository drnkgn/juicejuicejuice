package com.drnkgn.juicejuicejuice.db.converters

import androidx.room.TypeConverter
import com.drnkgn.juicejuicejuice.enums.TransactionType

class EnumConverters {
    @TypeConverter
    fun fromTransactionType(value: TransactionType?): String? {
        return value?.type
    }

    @TypeConverter
    fun toTransactionType(value: String?): TransactionType? {
        return TransactionType.entries.firstOrNull { it.type == value }
    }
}