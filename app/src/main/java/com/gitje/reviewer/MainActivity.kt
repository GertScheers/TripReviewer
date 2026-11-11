package com.gitje.reviewer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.gitje.reviewer.ui.Overview
import com.gitje.reviewer.ui.ReviewedItem
import com.gitje.reviewer.ui.theme.ReviewerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var showOverView by remember { mutableStateOf(true) }

            ReviewerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    if(showOverView)
                        Overview(Modifier.padding(innerPadding)) {
                            showOverView = false
                        }
                    else
                        ReviewedItem(Modifier.padding(innerPadding)) {
                            showOverView = true
                        }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ReviewerTheme {
        Greeting("Android")
    }
}