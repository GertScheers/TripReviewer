package com.gitje.reviewer.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.gitje.reviewer.models.ReviewableItem

@Dao
interface ReviewableItemDao {
    @Insert
    suspend fun insert(item: ReviewableItem): Long
    @Query("SELECT * FROM reviewable_items ORDER BY Date DESC")
    suspend fun getAll(): List<ReviewableItem>
    @Query("SELECT * FROM reviewable_items WHERE id = :id")
    suspend fun getById(id: Long): ReviewableItem?
    @Query("DELETE FROM reviewable_items WHERE id = :id")
    suspend fun deleteById(id: Long)
}