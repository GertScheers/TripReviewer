package com.gitje.reviewer.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.gitje.reviewer.models.ReviewEntry
import com.gitje.reviewer.models.ReviewableItem

@Dao
interface ReviewEntryDao {
    @Insert
    suspend fun insert(review: ReviewEntry): Long
    @Query(
        "SELECT * FROM review_entries " +
                "WHERE itemId = :itemId " +
                "ORDER BY id DESC"
    )
    suspend fun getForItem(itemId: Long): List<ReviewEntry>
    @Query("SELECT * FROM review_entries WHERE id = :id")
    suspend fun getById(id: Long): ReviewEntry?
    @Query("DELETE FROM review_entries WHERE id = :id")
    suspend fun deleteById(id: Long)
    @Query("SELECT * FROM review_entries")
    suspend fun getAll(): List<ReviewEntry>
}