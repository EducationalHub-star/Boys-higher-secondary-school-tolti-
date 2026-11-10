package com.example

import android.app.Application
import com.example.data.local.SchoolDatabase
import com.example.data.repository.SchoolRepository

class ToltiSchoolApp : Application() {

    val database: SchoolDatabase by lazy {
        SchoolDatabase.getInstance(this)
    }

    val repository: SchoolRepository by lazy {
        SchoolRepository(database)
    }

    override fun onCreate() {
        super.onCreate()
        com.example.service.RealtimeNotificationManager.createNotificationChannels(this)
    }
}
