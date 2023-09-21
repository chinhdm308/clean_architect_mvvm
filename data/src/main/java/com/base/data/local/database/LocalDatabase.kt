package com.base.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.base.data.local.dao.PostDao
import com.base.data.local.entities.PostEntity
import com.base.data.utils.CacheConstants
import com.base.data.utils.Migrations

@Database(
    entities = [PostEntity::class],
    version = Migrations.DB_VERSION,
    exportSchema = false
)
//@TypeConverters(ConverterHelper::class)
abstract class LocalDatabase : RoomDatabase() {

    abstract fun postDao(): PostDao

    companion object {
        @Volatile
        private var INSTANCE: LocalDatabase? = null

        @JvmStatic
        fun getInstance(applicationContext: Context): LocalDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(applicationContext).also { INSTANCE = it }
            }

        @JvmStatic
        private fun buildDatabase(applicationContext: Context) = Room.databaseBuilder(
            applicationContext,
            LocalDatabase::class.java,
            CacheConstants.DB_NAME
        ).build()
    }
}