# Android AI Agent Assistant Setup Guide

This folder contains the source code for your AI Agent Assistant App.

## 🛠️ Setup Instructions

1.  **Create a New Project** in Android Studio:
    - Select **Empty Activity** (Jetpack Compose).
    - Name: `AIAssistant`
    - Package Name: `com.example.aiassistant`
    - Language: **Kotlin**
    - Build Configuration: **Kotlin DSL (build.gradle.kts)**

2.  **Copy Files**:
    - Copy the contents of `android_src/app/src/main/java/com/example/aiassistant/` into your project's `app/src/main/java/com/example/aiassistant/` folder.
    - Shape:
      - `data/` -> `local/`, `remote/`, `repository/`
      - `domain/` -> `AgentTools.kt`
      - `ui/` -> `MainActivity.kt`, `screens/`, `viewmodel/`
      - `utils/` -> `Constants.kt`

3.  **Add Dependencies**:
    - Open `app/build.gradle.kts` in your project.
    - Copy the `dependencies` block from the provided `android_src/app/build.gradle.kts`.
    - Add the plugins: `id("com.google.devtools.ksp")` (You may need to add the classpath in project-level `build.gradle`).

4.  **Permissions**:
    - Update `AndroidManifest.xml` with permissions and `<queries>` tags provided in `android_src/app/src/main/AndroidManifest.xml`.

5.  **API Key**:
    - Open `utils/Constants.kt`.
    - Replace `YOUR_API_KEY_HERE` with your actual Google Gemini API Key.

## 🚀 Features Implemented

- **Chat Interface**: Material Design 3 UI with Bubble messages.
- **Voice Input**: Mic button uses Google Speech Recognition.
- **Agent Capabilities**:
  - **Call**: "Call 1234567890"
  - **WhatsApp**: "Send WhatsApp to 1234567890 saying Hello"
  - **Search**: "Search Google for Android development"
  - **Open App**: "Open YouTube"
  - **Alarm**: "Set alarm for 7 AM" (Try "Set alarm for Wake up at 7 0")
- **Persistence**: Chat history is saved locally using Room Database.

## ⚠️ Important Notes

- **Permissions**: The app requests `RECORD_AUDIO`, `CALL_PHONE`, and `SET_ALARM` on launch. Ensure you grant them.
- **WhatsApp**: To send a message without saving the number, we use the `api.whatsapp.com` deep link.
- **Package Visibility**: Android 11+ requires `<queries>` in Manifest to open specific apps. We've added basic ones.
