package com.example.aiassistant.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.aiassistant.data.local.dao.ChatDao
import com.example.aiassistant.data.local.entity.ChatMessage

@Database(entities = [ChatMessage::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun chatDao(): ChatDao
}
