package com.example.managementapp.presentation.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.addCallback
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.managementapp.presentation.navigation.ProjectGraph
import com.example.managementapp.presentation.theme.ManagementAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProjectsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ManagementAppTheme {
                Scaffold { innerPadding ->
                    ProjectGraph(
                        modifier = Modifier.padding(innerPadding),
                    )
                }
            }
        }
        onBackPressedDispatcher.addCallback(this) {
            finish()
        }
    }
}
