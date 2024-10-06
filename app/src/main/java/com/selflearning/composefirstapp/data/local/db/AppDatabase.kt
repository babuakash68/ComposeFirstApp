package com.selflearning.composefirstapp.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.selflearning.composefirstapp.data.local.dao.RepositoryDao
import com.selflearning.composefirstapp.data.local.entities.RepositoryEntity

@Database(entities = [RepositoryEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun repositoryDao(): RepositoryDao

    companion object {
        // Volatile annotation makes sure that the value of INSTANCE is always up-to-date and visible to other threads.
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // Method to get the singleton instance of the database
        fun getInstance(context: Context): AppDatabase {
            // Return the existing INSTANCE if it's not null, otherwise create it using synchronized
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database" // Name of the database file
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
