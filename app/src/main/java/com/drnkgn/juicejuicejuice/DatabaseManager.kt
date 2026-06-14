package com.drnkgn.juicejuicejuice

import com.drnkgn.juicejuicejuice.db.AppDatabase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DatabaseManager @Inject constructor(
    private val factory: @JvmSuppressWildcards () -> AppDatabase
) {
    @Volatile
    private var db: AppDatabase = factory()

    fun getDb(): AppDatabase = db

    fun rebuild() {
        db.close()
        db = factory()
    }
}