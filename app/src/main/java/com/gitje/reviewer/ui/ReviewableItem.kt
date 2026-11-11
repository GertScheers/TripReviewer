package com.gitje.reviewer.ui

import androidx.activity.compose.BackHandler
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSliderState
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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gitje.reviewer.R
import com.gitje.reviewer.logic.GeneralViewModel
import com.gitje.reviewer.models.ReviewEntry
import com.gitje.reviewer.models.ReviewType
import com.gitje.reviewer.models.ReviewableItem
import com.gitje.reviewer.models.Reviewer
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDate
import java.util.Locale

@Composable
fun ReviewedItem(modifier: Modifier, backToOverview: () -> Unit) {
    val generalViewModel = koinViewModel<GeneralViewModel>()
    val allReviewEntries by generalViewModel.reviewsForItem.collectAsState()
    val activeReviewableItem by generalViewModel.activeReviewableItem.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    BackHandler {
        backToOverview()
    }

    ReviewedItemContent(
        modifier = modifier,
        item = activeReviewableItem!!,
        allReviewEntries = allReviewEntries,
        addReviewEntry = { rating, reviewer, input ->
            coroutineScope.launch {
                generalViewModel.addReviewEntry(
                    rating,
                    reviewer,
                    input
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewedItemContent(
    modifier: Modifier,
    item: ReviewableItem,
    allReviewEntries: List<ReviewEntry>,
    addReviewEntry: (Double, Reviewer, String) -> Unit
) {
    var showNewEntry by remember { mutableStateOf(false) }
    var reviewer by remember { mutableStateOf(Reviewer.Reviewer) }
    var input by remember { mutableStateOf(TextFieldValue("")) }
    val ratingState = rememberSliderState(5f, 99, valueRange = 0f..10f)

    LaunchedEffect(showNewEntry) {
        //Reset  values when hiding the new entry
        if (!showNewEntry) {
            reviewer = Reviewer.Reviewer
            input = TextFieldValue("")
            ratingState.value = 5f
        }
    }

    Box(
        modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        LazyColumn(Modifier.fillMaxSize()) {
            stickyHeader {
                Row(Modifier.fillMaxWidth()) {
                    Text(item.location, fontSize = 24.sp, maxLines = 2, modifier = Modifier.weight(1f))
                    Text("${item.date}", fontSize = 24.sp)
                }
                Spacer(Modifier.height(8.dp))
            }

            item {
                if (showNewEntry) {
                    Card(
                        Modifier
                            .fillMaxWidth()
                    ) {
                        Column(
                            Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                String.format(Locale.getDefault(), "%.1f", ratingState.value),
                                fontSize = 32.sp
                            )
                            Spacer(Modifier.height(16.dp))
                            Slider(
                                ratingState,
                                modifier = Modifier.padding(horizontal = 12.dp),
                                colors = SliderDefaults.colors(
                                    activeTickColor = Color.Transparent,
                                    inactiveTickColor = Color.Transparent
                                )
                            )
                        }

                        Spacer(Modifier.height(16.dp))

                        SingleChoiceSegmentedButtonRow(
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp)
                        ) {
                            SegmentedButton(reviewer == Reviewer.Phyllis, onClick = {
                                reviewer = Reviewer.Phyllis
                            }, shape = SegmentedButtonDefaults.itemShape(0, 4)) {
                                Text("Phyllis")
                            }
                            SegmentedButton(reviewer == Reviewer.Tom, onClick = {
                                reviewer = Reviewer.Tom
                            }, shape = SegmentedButtonDefaults.itemShape(1, 4)) {
                                Text("Tom")
                            }
                            SegmentedButton(reviewer == Reviewer.Mary, onClick = {
                                reviewer = Reviewer.Mary
                            }, shape = SegmentedButtonDefaults.itemShape(2, 4)) {
                                Text("Mary")
                            }
                            SegmentedButton(reviewer == Reviewer.Gert, onClick = {
                                reviewer = Reviewer.Gert
                            }, shape = SegmentedButtonDefaults.itemShape(3, 4)) {
                                Text("Gert")
                            }
                        }

                        Spacer(Modifier.height(16.dp))

                        OutlinedTextField(
                            value = input,
                            { input = it },
                            label = {
                                Text("Thoughts?")
                            },
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(horizontal = 6.dp)
                        )

                        Spacer(Modifier.height(16.dp))

                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
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
                                enabled = reviewer != Reviewer.Reviewer,
                                onClick = {
                                    addReviewEntry(
                                        ratingState.value.toDouble(),
                                        reviewer,
                                        input.text
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

            items(allReviewEntries) {
                Card(Modifier.padding(8.dp)) {
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
                                    imageVector = ImageVector.vectorResource(R.drawable.ic_person),
                                    contentDescription = "reviewer"
                                )

                                Text(it.reviewer.name)
                            }

                            Spacer(Modifier.height(8.dp))

                            Text(
                                it.text,
                                modifier = Modifier
                                    .padding(8.dp)
                            )
                        }

                        Column {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.ic_rating),
                                contentDescription = "rating"
                            )
                            Text(String.format(Locale.getDefault(), "%.1f", it.rating))
                        }
                    }
                }
            }
        }

        if (allReviewEntries.isEmpty() && !showNewEntry)
            Text("No reviews yet", modifier = Modifier.align(Alignment.Center))

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

@Preview
@Composable
fun ReviewedItemPreview() {
    ReviewedItemContent(
        Modifier,
        ReviewableItem(location = "", date = LocalDate.now(), type = ReviewType.FOOD),
        emptyList()
    ) { _, _, _ -> }
}