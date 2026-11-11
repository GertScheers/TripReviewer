package com.gitje.reviewer.logic

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.gitje.reviewer.models.ReviewEntry
import com.gitje.reviewer.models.ReviewType
import com.gitje.reviewer.models.ReviewableItem
import com.gitje.reviewer.models.Reviewer
import kotlinx.coroutines.launch
import java.time.LocalDate

class GeneralViewModel(
    application: Application,
    private val generalRepository: GeneralRepository
) : AndroidViewModel(application) {
    val reviewableItems = generalRepository.allReviewableItems
    val reviewsForItem = generalRepository.reviewsForItem
    val activeReviewableItem = generalRepository.activeReviewableItem

    var selectedReviewableItemId: Long? = null

    init {
        viewModelScope.launch {
            generalRepository.getAllReviewableItems()
        }
    }

    suspend fun addNewReviewableItem(
        location: String,
        date: LocalDate,
        type: ReviewType
    ) {
        generalRepository.insertNewReviewableItem(
            ReviewableItem(
                location = location,
                date = date,
                type = type
            )
        )
    }

    suspend fun addReviewEntry(
        rating: Double,
        reviewer: Reviewer,
        input: String
    ) {
        selectedReviewableItemId?.let {
            generalRepository.insertNewReviewEntry(
                ReviewEntry(
                    rating = rating,
                    reviewer = reviewer,
                    text = input,
                    itemId = it
                )
            )
            generalRepository.getReviewsForItem(it)
        }
    }

    suspend fun getReviewsForItem(id: Long) {
        selectedReviewableItemId = id
        generalRepository.getReviewsForItem(id)
    }
}