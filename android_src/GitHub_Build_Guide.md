# ☁️ How to Build Your App (No Android Studio Required)

Since you don't have Android Studio, we will use **GitHub** to build the app for you.

## Step 1: Create a GitHub Account

1.  Go to [github.com](https://github.com/).
2.  Sign up for a free account if you don't have one.

## Step 2: Create a New Repository

1.  Click the **+** icon in the top-right corner and select **New repository**.
2.  **Repository name**: `ai-assistant-android`.
3.  **Public/Private**: Choose **Public** (easiest) or **Private** (secure).
4.  **Initialize**: Do **NOT** check "Add a README" or ".gitignore". Keep it empty.
5.  Click **Create repository**.

## Step 3: Upload Files

1.  On the new repository page, click the link that says **"uploading an existing file"**.
2.  Open your computer's file explorer to the `android_src` folder I created for you.
3.  **Drag and Drop** ALL files and folders from `android_src` into the GitHub upload area.
    - _Important_: Ensure `.github` folder is uploaded! (You might need to enable "Show Hidden Files" in Windows View settings).
4.  Wait for the upload to finish.
5.  Type "Initial commit" in the "Commit changes" box and click **Commit changes**.

## Step 4: Wait for Build

1.  Click the **Actions** tab at the top of your repository page.
2.  You should see a workflow named **Build Android APK** running (yellow circle).
3.  Click on it to see progress. It will take 2-5 minutes.

## Step 5: Download APK

1.  Once the circle turns **Green (Success)**, click on the workflow run.
2.  Scroll down to the **Artifacts** section at the bottom.
3.  Click on **ai-assistant-debug-apk**.
4.  This will download a `.zip` file. Extract it to find `app-debug.apk`.
5.  Send this APK to your phone and install it! 🚀

## ⚠️ Important Configuration

Before uploading, make sure you have added your **Gemini API Key** in `app/src/main/java/com/example/aiassistant/utils/Constants.kt`. GitHub will build exactly what you upload.
