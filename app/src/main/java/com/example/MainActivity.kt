package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.data.firebase.configureAppCheck
import com.example.ui.SolaceApp
import com.example.ui.viewmodel.SolaceViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: SolaceViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SolaceApplication.initFirebase(application)
        // Step 1: Configure App Check with launch intent before any UI or Firebase operations
        configureAppCheck(this, intent)

        enableEdgeToEdge()
        setContent {
            SolaceApp(viewModel = viewModel)
        }
    }
}
