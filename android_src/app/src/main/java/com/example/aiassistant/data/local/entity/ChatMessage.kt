package com.example.aiassistant.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat_messages")
data class ChatMessage(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val role: String, // "user" or "model"
    val content: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isPending: Boolean = false // For handling loading state/optimistic updates
)
