package com.gitje.reviewer.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.gitje.reviewer.models.ReviewEntry
import com.gitje.reviewer.models.ReviewableItem

@Database(
    entities = [
        ReviewableItem::class,
        ReviewEntry::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(ReviewDatabaseConverters::class)
abstract class ReviewDatabase : RoomDatabase() {
    abstract fun reviewableItemDao(): ReviewableItemDao
    abstract fun reviewEntryDao(): ReviewEntryDao

    companion object {
        fun getDatabase(context: Context): ReviewDatabase {
            return Room.databaseBuilder(
                        context.applicationContext,
                        ReviewDatabase::class.java,
                        "db_expensium"
                    ).fallbackToDestructiveMigration(false).build()
        }
    }
}