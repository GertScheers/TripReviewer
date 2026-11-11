package com.gitje.reviewer.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "review_entries",
    foreignKeys = [
        ForeignKey(
            entity = ReviewableItem::class,
            parentColumns = ["id"],
            childColumns = ["itemId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ReviewEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val rating: Double,
    //Reviewer
    val reviewer: Reviewer,
    val text: String,
    val itemId: Long
)