package com.example.aiassistant.ui

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.example.aiassistant.ui.screens.ChatScreen
import com.example.aiassistant.ui.viewmodel.ChatViewModel
import com.permissionx.guolindev.PermissionX // Optional: or use standard launcher

class MainActivity : ComponentActivity() {
    
    // Using standard ViewModel provider
    private val viewModel: ChatViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Request Permissions
        // For simplicity in this generated code, using a helper approach or standard
        // Here is standard Activity Result API approach
        requestPermissions()

        setContent {
            MaterialTheme {
                Surface {
                    ChatScreen(viewModel = viewModel)
                }
            }
        }
    }
    
    private fun requestPermissions() {
        requestPermissions(
            arrayOf(
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.CALL_PHONE,
                Manifest.permission.SET_ALARM
            ), 
            101
        )
    }
}
