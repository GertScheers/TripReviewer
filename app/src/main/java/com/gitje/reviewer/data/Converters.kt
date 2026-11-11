package com.gitje.reviewer.data

import androidx.room.TypeConverter
import com.gitje.reviewer.models.ReviewType
import com.gitje.reviewer.models.Reviewer
import java.time.LocalDate

class ReviewDatabaseConverters {

    @TypeConverter
    fun fromTimestamp(value: Long?): LocalDate? {
        return value?.let { LocalDate.ofEpochDay(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: LocalDate?): Long? {
        return date?.toEpochDay()
    }

    @TypeConverter
    fun fromReviewType(type: ReviewType?): String? {
        return type?.name
    }

    @TypeConverter
    fun reviewTypeToString(value: String?): ReviewType? {
        return value?.let { ReviewType.valueOf(it) }
    }

    @TypeConverter
    fun fromReviewer(reviewer: Reviewer?): String? {
        return reviewer?.name
    }

    @TypeConverter
    fun reviewerToString(value: String?): Reviewer? {
        return value?.let { Reviewer.valueOf(it) }
    }
}