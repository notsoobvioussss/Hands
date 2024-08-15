package com.timur.hands

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.timur.hands.ui.CellWorldScreen
import com.timur.hands.ui.theme.HandsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HandsTheme {
                CellWorldScreen()
            }
        }
    }
}