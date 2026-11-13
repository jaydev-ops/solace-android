package com.example

import android.app.Application
import android.content.Context
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions

class SolaceApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initFirebase(this)
    }

    companion object {
        private const val TAG = "SolaceApplication"

        fun initFirebase(context: Context) {
            try {
                if (FirebaseApp.getApps(context).isEmpty()) {
                    val app = try {
                        FirebaseApp.initializeApp(context)
                    } catch (e: Exception) {
                        Log.w(TAG, "Standard Firebase initialization threw exception", e)
                        null
                    }

                    if (app == null) {
                        Log.i(TAG, "google-services.json not found, initializing with fallback FirebaseOptions")
                        val options = FirebaseOptions.Builder()
                            .setApplicationId(context.packageName)
                            .setApiKey("AIzaSyDummyKeyForOfflineUseOnly000000000")
                            .setProjectId("ai-studio-solace-sanctuary")
                            .setDatabaseUrl("https://ai-studio-solace-sanctuary.firebaseio.com")
                            .build()
                        FirebaseApp.initializeApp(context, options)
                        Log.i(TAG, "Firebase initialized successfully with fallback options")
                    } else {
                        Log.i(TAG, "Firebase initialized from google-services.json")
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Firebase initialization failed", e)
            }
        }
    }
}
