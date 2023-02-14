package com.tt.imposteroslauncher.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tt.imposteroslauncher.model.TestModel

@Database(
    entities = [
        TestModel::class
    ],
    version = 1,
    autoMigrations = [
    ],
    exportSchema = true
)
abstract class LauncherDatabase : RoomDatabase() {

    abstract fun launcherDao(): LauncherDao

    companion object {
        private var INSTANCE: LauncherDatabase? = null
        fun getDatabase(context: Context): LauncherDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE = Room.databaseBuilder(
                        context,
                        LauncherDatabase::class.java,
                        "launcher_database"
                    ).build()
                }
            }
            return INSTANCE!!
        }
    }
}