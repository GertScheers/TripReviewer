package com.gitje.reviewer.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "reviewable_items")
data class ReviewableItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val location: String,
    val date: LocalDate,
    val type: ReviewType
)
