package com.gitje.reviewer.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.getSelectedDate
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gitje.reviewer.R
import com.gitje.reviewer.logic.GeneralViewModel
import com.gitje.reviewer.models.ReviewType
import com.gitje.reviewer.models.ReviewableItem
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDate

@Composable
fun Overview(
    modifier: Modifier,
    goToReviewEntries: () -> Unit
) {
    val generalViewModel = koinViewModel<GeneralViewModel>()
    val allReviewEntries by generalViewModel.reviewableItems.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    OverviewContent(
        modifier,
        allReviewEntries = allReviewEntries,
        addNewItem = { location, date, reviewType ->
            coroutineScope.launch {
                generalViewModel.addNewReviewableItem(
                    location,
                    date,
                    reviewType
                )
            }
        },
        goToReviewEntries = {
            coroutineScope.launch {
                generalViewModel.getReviewsForItem(it)
                goToReviewEntries()
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OverviewContent(
    modifier: Modifier,
    allReviewEntries: List<ReviewableItem>,
    addNewItem: (String, LocalDate, ReviewType) -> Unit,
    goToReviewEntries: (Long) -> Unit
) {
    var locationTextFieldValue by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        initialSelectedDate = null
    )
    var selectedDate by remember(datePickerState.selectedDateMillis) {
        mutableStateOf(
            datePickerState.getSelectedDate()
        )
    }
    var selectedReviewType by remember { mutableStateOf(ReviewType.HOTEL) }
    var showNewEntry by remember { mutableStateOf(false) }

    LaunchedEffect(showNewEntry) {
        //Reset entries when hiding the new entry
        if (!showNewEntry) {
            locationTextFieldValue = ""
            datePickerState.selectedDateMillis = null
            selectedReviewType = ReviewType.HOTEL
        }
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                Text(
                    "Ok",
                    Modifier.clickable(onClick = {
                        showDatePicker = false
                    })
                )
            },
            modifier = Modifier.padding(10.dp)
        ) {
            DatePicker(
                state = datePickerState,
                title = null,
            )
        }
    }

    Box(modifier.fillMaxSize()) {
        LazyColumn(Modifier.fillMaxSize()) {
            item {
                if (showNewEntry) {
                    Card(
                        Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 8.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(R.drawable.ic_location),
                                    contentDescription = "location"
                                )

                                TextField(locationTextFieldValue, { locationTextFieldValue = it })
                            }
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 8.dp)
                                    .clickable(onClick = { showDatePicker = true }),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(R.drawable.ic_calendar),
                                    contentDescription = "date"
                                )
                                Text("${selectedDate ?: "Set date"}")
                            }
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 8.dp),
                                horizontalArrangement = Arrangement.SpaceAround,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Icon(
                                        imageVector = ImageVector.vectorResource(R.drawable.ic_hotel),
                                        contentDescription = "hotel",
                                        modifier = Modifier
                                            .size(if (selectedReviewType == ReviewType.HOTEL) 42.dp else 30.dp)
                                            .clickable(onClick = {
                                                selectedReviewType = ReviewType.HOTEL
                                            })
                                    )
                                    if (selectedReviewType == ReviewType.HOTEL)
                                        Box(
                                            Modifier
                                                .width(42.dp)
                                                .height(2.dp)
                                                .background(Color.Green)
                                        )
                                }
                                Column {
                                    Icon(
                                        imageVector = ImageVector.vectorResource(R.drawable.ic_activity),
                                        contentDescription = "activity",
                                        modifier = Modifier
                                            .size(if (selectedReviewType == ReviewType.ACTIVITY) 42.dp else 30.dp)
                                            .clickable(onClick = {
                                                selectedReviewType = ReviewType.ACTIVITY
                                            })
                                    )
                                    if (selectedReviewType == ReviewType.ACTIVITY)
                                        Box(
                                            Modifier
                                                .width(42.dp)
                                                .height(2.dp)
                                                .background(Color.Green)
                                        )
                                }
                                Column {
                                    Icon(
                                        imageVector = ImageVector.vectorResource(R.drawable.ic_food),
                                        contentDescription = "food",
                                        modifier = Modifier
                                            .size(if (selectedReviewType == ReviewType.FOOD) 42.dp else 30.dp)
                                            .clickable(onClick = {
                                                selectedReviewType = ReviewType.FOOD
                                            })
                                    )
                                    if (selectedReviewType == ReviewType.FOOD)
                                        Box(
                                            Modifier
                                                .width(42.dp)
                                                .height(2.dp)
                                                .background(Color.Green)
                                        )
                                }
                            }

                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(top = 12.dp),
                                horizontalArrangement = Arrangement.End
                            ) {
                                Button(
                                    onClick = { },
                                    shape = CircleShape,
                                    modifier = Modifier.size(50.dp),
                                    contentPadding = PaddingValues(0.dp),
                                    colors = ButtonDefaults.buttonColors()
                                        .copy(containerColor = Color.Red)
                                ) {
                                    Icon(
                                        imageVector = ImageVector.vectorResource(R.drawable.ic_no),
                                        contentDescription = "close"
                                    )
                                }

                                Spacer(Modifier.width(8.dp))

                                Button(
                                    enabled = datePickerState.selectedDateMillis != null
                                            && locationTextFieldValue.isNotEmpty(),
                                    onClick = {
                                        addNewItem(
                                            locationTextFieldValue,
                                            datePickerState.getSelectedDate()!!,
                                            selectedReviewType
                                        )
                                        showNewEntry = false
                                    },
                                    modifier = Modifier.size(50.dp),
                                    contentPadding = PaddingValues(0.dp),
                                    shape = CircleShape,
                                    colors = ButtonDefaults.buttonColors()
                                        .copy(containerColor = Color.Green)
                                ) {
                                    Icon(
                                        imageVector = ImageVector.vectorResource(R.drawable.ic_ok),
                                        contentDescription = "ok",
                                        tint = Color.Black
                                    )
                                }
                            }
                        }
                    }
                }
            }

            items(allReviewEntries) {
                Card(
                    Modifier
                        .padding(8.dp)
                        .clickable(onClick = {
                            goToReviewEntries(it.id)
                        })
                ) {
                    Row(
                        Modifier
                            .padding(8.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Row(
                                Modifier
                                    .padding(horizontal = 8.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(R.drawable.ic_location),
                                    contentDescription = "location"
                                )

                                Text(it.location)
                            }

                            Spacer(Modifier.height(8.dp))

                            Row(
                                Modifier
                                    .padding(horizontal = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(R.drawable.ic_calendar),
                                    contentDescription = "date"
                                )
                                Text("${it.date}")
                            }
                        }

                        Icon(
                            imageVector = ImageVector.vectorResource(
                                when (it.type) {
                                    ReviewType.HOTEL -> R.drawable.ic_hotel
                                    ReviewType.ACTIVITY -> R.drawable.ic_activity
                                    ReviewType.FOOD -> R.drawable.ic_food
                                }
                            ),
                            modifier = Modifier.size(50.dp),
                            contentDescription = null
                        )
                    }
                }
            }
        }

        if (allReviewEntries.isEmpty() && !showNewEntry)
            Text("No entries yet", modifier = Modifier.align(Alignment.Center))

        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(12.dp),
            onClick = {
                showNewEntry = true
            }) {
            Text("+", fontSize = 32.sp)
        }
    }
}

@Composable
@Preview
fun OverviewPreview() {
    MaterialTheme {
        OverviewContent(Modifier.background(Color.White), emptyList(), { _, _, _ -> }, {})
    }
}