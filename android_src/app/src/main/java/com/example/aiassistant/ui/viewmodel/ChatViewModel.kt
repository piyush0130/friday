package com.example.aiassistant.ui.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.aiassistant.data.local.AppDatabase
import com.example.aiassistant.data.local.entity.ChatMessage
import com.example.aiassistant.data.remote.api.GeminiApiService
import com.example.aiassistant.data.repository.ChatRepository
import com.example.aiassistant.domain.AgentTools
import com.example.aiassistant.utils.Constants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ChatViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ChatRepository
    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        // Initialize Database
        val database = androidx.room.Room.databaseBuilder(
            application,
            AppDatabase::class.java,
            "chat_database"
        ).build()

        // Initialize Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val apiService = retrofit.create(GeminiApiService::class.java)

        repository = ChatRepository(database.chatDao(), apiService, Constants.API_KEY)

        // Observe messages
        viewModelScope.launch {
            repository.messages.collectLatest {
                _messages.value = it
                // Check latest message for commands if it's from model
                if (it.isNotEmpty()) {
                    val lastMsg = it.last()
                    if (lastMsg.role == "model") {
                        parseAndExecuteCommand(application, lastMsg.content)
                    }
                }
            }
        }
    }

    fun sendMessage(text: String) {
        if (text.isBlank()) return
        viewModelScope.launch {
            _isLoading.value = true
            repository.sendMessage(text, _messages.value)
            _isLoading.value = false
        }
    }

    private fun parseAndExecuteCommand(context: Context, content: String) {
        // Regex patterns for commands
        val callRegex = Regex("\\[CALL: (.*?)\\]")
        val searchRegex = Regex("\\[SEARCH: (.*?)\\]")
        val whatsappRegex = Regex("\\[WHATSAPP: (.*?)\\|(.*?)\\]")
        val openRegex = Regex("\\[OPEN: (.*?)\\]")
        val alarmRegex = Regex("\\[ALARM: (.*?)\\|(.*?)\\|(.*?)\\]")

        callRegex.find(content)?.let {
            val number = it.groupValues[1]
            AgentTools.makePhoneCall(context, number.trim())
        }

        searchRegex.find(content)?.let {
            val query = it.groupValues[1]
            AgentTools.openGoogleSearch(context, query.trim())
        }

        whatsappRegex.find(content)?.let {
            val number = it.groupValues[1]
            val message = it.groupValues[2]
            AgentTools.sendWhatsAppMessage(context, number.trim(), message.trim())
        }

        openRegex.find(content)?.let {
            val pkg = it.groupValues[1]
            AgentTools.openApp(context, pkg.trim())
        }
        
        alarmRegex.find(content)?.let {
             val msg = it.groupValues[1]
             val hour = it.groupValues[2].toIntOrNull() ?: 9
             val min = it.groupValues[3].toIntOrNull() ?: 0
             AgentTools.setAlarm(context, msg, hour, min)
        }
    }
}
