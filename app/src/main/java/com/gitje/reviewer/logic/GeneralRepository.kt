package com.gitje.reviewer.logic

import com.gitje.reviewer.data.ReviewDatabase
import com.gitje.reviewer.models.ReviewEntry
import com.gitje.reviewer.models.ReviewableItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class GeneralRepository(
    database: ReviewDatabase
) {
    private val _allReviewableItems = MutableStateFlow<List<ReviewableItem>>(emptyList())
    val allReviewableItems: StateFlow<List<ReviewableItem>> = _allReviewableItems

    private val _reviewsForItem = MutableStateFlow<List<ReviewEntry>>(emptyList())
    val reviewsForItem: StateFlow<List<ReviewEntry>> = _reviewsForItem
    private val _activeReviewableItem = MutableStateFlow<ReviewableItem?>(null)
    val activeReviewableItem: StateFlow<ReviewableItem?> = _activeReviewableItem

    val reviewableItemsDao = database.reviewableItemDao()
    val reviewEntryDao = database.reviewEntryDao()

    suspend fun getAllReviewableItems() {
        _allReviewableItems.value = reviewableItemsDao.getAll()
    }

    suspend fun insertNewReviewableItem(item: ReviewableItem) {
        reviewableItemsDao.insert(item)
        getAllReviewableItems()
    }

    suspend fun insertNewReviewEntry(entry: ReviewEntry) {
        reviewEntryDao.insert(entry)
    }

    suspend fun getReviewsForItem(id: Long) {
        _activeReviewableItem.value = reviewableItemsDao.getById(id)
        _reviewsForItem.value = reviewEntryDao.getForItem(id)
    }
}