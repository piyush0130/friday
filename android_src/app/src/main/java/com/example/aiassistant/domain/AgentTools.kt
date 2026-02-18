package com.example.aiassistant.domain

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.AlarmClock
import android.widget.Toast

object AgentTools {

    fun executeAction(context: Context, actionDetails: String) {
        // Simple logic to parse action from AI response if it follows a specific pattern
        // Or if the AI describes what it wants to do.
        // For this example, we'll assume the AI response MIGHT contain a command line like:
        // [CALL: 1234567890] or [SEARCH: something]
        // In a real app, you'd use Function Calling.
        
        // Here we provide direct functions to be called by the ViewModel based on intent analysis.
    }

    fun makePhoneCall(context: Context, phoneNumber: String) {
        try {
            val intent = Intent(Intent.ACTION_CALL).apply {
                data = Uri.parse("tel:$phoneNumber")
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        } catch (e: SecurityException) {
            Toast.makeText(context, "Permission denied to make calls", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(context, "Error making call: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    fun sendWhatsAppMessage(context: Context, phoneNumber: String, message: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("https://api.whatsapp.com/send?phone=$phoneNumber&text=$message")
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "WhatsApp not installed or error", Toast.LENGTH_SHORT).show()
        }
    }

    fun openGoogleSearch(context: Context, query: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/search?q=$query"))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "Browser not found", Toast.LENGTH_SHORT).show()
        }
    }

    fun setAlarm(context: Context, messsage: String, hour: Int, minute: Int) {
         val intent = Intent(AlarmClock.ACTION_SET_ALARM).apply {
            putExtra(AlarmClock.EXTRA_MESSAGE, messsage)
            putExtra(AlarmClock.EXTRA_HOUR, hour)
            putExtra(AlarmClock.EXTRA_MINUTES, minute)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
        } else {
             Toast.makeText(context, "No Alarm app found", Toast.LENGTH_SHORT).show()
        }
    }

    fun openApp(context: Context, packageName: String) {
        val intent = context.packageManager.getLaunchIntentForPackage(packageName)
        if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        } else {
            Toast.makeText(context, "App not found: $packageName", Toast.LENGTH_SHORT).show()
        }
    }
}
