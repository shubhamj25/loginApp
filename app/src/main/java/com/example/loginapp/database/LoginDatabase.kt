package com.example.loginapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


@Database(entities = [LoginEntity::class], version = 1, exportSchema = false)
abstract class LoginDatabase : RoomDatabase() {
    abstract val loginDatabaseDao: LoginDatabaseDao
    companion object {
        private val migration12: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
//                database.execSQL(
//                    "ALTER TABLE registered_users_table "
//                            + " ADD COLUMN address LONGTEXT"
//                )
            }
        }
        @Volatile
        private var INSTANCE: LoginDatabase? = null
        fun getInstance(context: Context): LoginDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        LoginDatabase::class.java, "login_app_database"
                    ).addMigrations(migration12).build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}