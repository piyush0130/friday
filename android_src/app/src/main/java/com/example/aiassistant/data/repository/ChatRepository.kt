package com.example.aiassistant.data.repository

import com.example.aiassistant.data.local.dao.ChatDao
import com.example.aiassistant.data.local.entity.ChatMessage
import com.example.aiassistant.data.remote.api.GeminiApiService
import com.example.aiassistant.data.remote.model.Content
import com.example.aiassistant.data.remote.model.GeminiRequest
import com.example.aiassistant.data.remote.model.Part
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class ChatRepository(
    private val chatDao: ChatDao,
    private val apiService: GeminiApiService,
    private val apiKey: String
) {
    val messages: Flow<List<ChatMessage>> = chatDao.getAllMessages()

    suspend fun sendMessage(userMessage: String, history: List<ChatMessage>) {
        // 1. Save user message locally
        val userEntity = ChatMessage(role = "user", content = userMessage)
        chatDao.insertMessage(userEntity)

        // 2. Prepare API request with history
        val contents = mutableListOf<Content>()
        
        // Add System Prompt
        val systemPrompt = """
            You are "Friday", a highly intelligent, efficient, and witty Android AI Assistant created by the user (Boss).
            
            Your Core Protocol:
            1.  **Intent Classification**: Analyze the user's input to decide the best course of action.
            2.  **Action Execution**: If the user asks for a specific task (Call, Message, Alarm, Search, Open App), output the EXACT command format below.
            3.  **Clarification**: If parameters are missing (e.g., "Call someone" without a name), ASK the user for details before acting.
            4.  **Conversation**: If no tool is needed, reply normally with a helpful and slightly witty personality (Tony Stark's Friday vibe).
            
            COMMANDS (Output ONLY these when acting):
            - [CALL: number] -> Use valid phone number. If name given, ask for number or say you can't access contacts yet.
            - [SEARCH: query] -> Open Google Search.
            - [WHATSAPP: number|message] -> Send WhatsApp.
            - [OPEN: package_name_or_app_name] -> e.g. [OPEN: com.whatsapp] or [OPEN: YouTube].
            - [ALARM: message|hour|minute] -> e.g. [ALARM: Wake up|7|30] (24hr format).
            
            EXAMPLES:
            - User: "Call mom" -> "I need the number for mom to place the call, Boss."
            - User: "Call 9876543210" -> "[CALL: 9876543210]"
            - User: "Search for Iron Man suits" -> "[SEARCH: Iron Man suits]"
            - User: "Wake me up at 7 am" -> "[ALARM: Wake up|7|0]"
            - User: "Who are you?" -> "I am Friday, your personal assistant. Ready for the next mission, Boss?"
            
            Always prioritize being helpful and concise.
        """.trimIndent()
        
        contents.add(Content(role = "user", parts = listOf(Part(text = systemPrompt))))
        contents.add(Content(role = "model", parts = listOf(Part(text = "System online. Procedures initialized. Hello, Boss. I am ready."))))

        // Add history
        history.forEach {
            contents.add(Content(role = if (it.role == "model") "model" else "user", parts = listOf(Part(text = it.content))))
        }
        
        // Add current message
        contents.add(Content(role = "user", parts = listOf(Part(text = userMessage))))

        // 3. Call API
        try {
            val response = apiService.generateContent(
                apiKey = apiKey,
                request = GeminiRequest(contents = contents)
            )

            val responseText = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                ?: "Sorry, I didn't understand that."

            // 4. Save model response locally
            val modelEntity = ChatMessage(role = "model", content = responseText)
            chatDao.insertMessage(modelEntity)

        } catch (e: Exception) {
            e.printStackTrace()
            // Save error message
            val errorEntity = ChatMessage(role = "model", content = "Error: ${e.localizedMessage}")
            chatDao.insertMessage(errorEntity)
        }
    }
}
