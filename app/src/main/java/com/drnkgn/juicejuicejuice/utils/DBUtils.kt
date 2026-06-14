package com.drnkgn.juicejuicejuice.utils

import android.database.sqlite.SQLiteDatabase
import android.util.Log
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteDatabase
import com.drnkgn.juicejuicejuice.db.AppDatabase
import com.drnkgn.juicejuicejuice.db.dto.ColumnInfo
import com.drnkgn.juicejuicejuice.db.dto.TableInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

object DBUtils {
    fun getAllTableNames(db: SupportSQLiteDatabase): List<String> {
        val cursor = db.query("""
            SELECT name 
            FROM sqlite_master 
            WHERE type = 'table' 
            AND name NOT LIKE 'sqlite_%'
        """.trimIndent())

        return cursor.use {
            buildList {
                while (it.moveToNext()) {
                    add(it.getString(0))
                }
            }
        }
    }

    fun getTableInfo(db: SupportSQLiteDatabase, table: String): List<ColumnInfo> {
        val cursor = db.query("PRAGMA table_info($table)")
        return cursor.use {
            buildList {
                while (it.moveToNext()) {
                    add(
                        ColumnInfo(
                            name = it.getString(1),
                            type = it.getString(2),
                            notNull = it.getInt(3) == 1,
                            primaryKey = it.getInt(5) > 0
                        )
                    )
                }
            }
        }
    }

    fun buildSchema(db: SupportSQLiteDatabase): List<TableInfo> {
        val excludeTables = setOf("android_metadata", "room_master_table")
        val tables = getAllTableNames(db).filterNot { it in excludeTables }

        return tables.map {
            TableInfo(
                name = it,
                columns = getTableInfo(db, it)
            )
        }
    }

    suspend fun checkpoint(database: AppDatabase) = withContext(Dispatchers.IO) {
        val cursor = database.query(SimpleSQLiteQuery("PRAGMA wal_checkpoint(FULL)"))
        if (cursor.moveToFirst() && cursor.getInt(0) == 1) {
            throw RuntimeException("Checkpoint was blocked from completing")
        }

    }

    fun validateDatabaseSchema(
        appDatabase: AppDatabase,
        dbFile: File,
        enableLogging: Boolean = false
    ): Boolean {
        val db = SQLiteDatabase.openDatabase(dbFile.absolutePath, null, SQLiteDatabase.OPEN_READONLY)
        val schema = this.buildSchema(appDatabase.openHelper.readableDatabase)

        return try {
            for (table in schema) {
                val cursor = db.rawQuery("PRAGMA table_info(${table.name})", null)

                if (cursor.count == 0) {
                    if (enableLogging) {
                        Log.e("JJJ", "Table ${table.name} not found")
                    }

                    return false
                } else {
                    if (enableLogging) {
                        Log.i("JJJ", "Table ${table.name} found")
                    }
                }

                val actualColumns = mutableSetOf<String>()
                cursor.use {
                    while (it.moveToNext()) {
                        actualColumns.add(it.getString(1))
                    }
                }

                for (column in table.columns) {
                    if (!actualColumns.contains(column.name)) {
                        if (enableLogging) {
                            Log.e("JJJ", " - Column ${column.name} not found in ${table.name}")
                        }

                        return false
                    } else {
                        if (enableLogging) {
                            Log.i("JJJ", " - Column ${column.name} found in ${table.name}")
                        }
                    }
                }
            }
            true
        } catch (e: Exception) {
            Log.e("JJJ", "Validation failed: ${e.message}")
            false
        } finally {
            db.close()
        }
    }
}