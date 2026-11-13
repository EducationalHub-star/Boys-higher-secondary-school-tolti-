package com.example

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.os.Build
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import coil.memory.MemoryCache
import com.example.data.local.SchoolDatabase
import com.example.data.repository.SchoolRepository

class ToltiSchoolApp : Application(), ImageLoaderFactory {

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

    override fun newImageLoader(): ImageLoader {
        val am = getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager
        val isLowRam = (am?.isLowRamDevice == true) || ((am?.memoryClass ?: 256) <= 128)
        val useRgb565 = isLowRam || Build.VERSION.SDK_INT < Build.VERSION_CODES.O

        return ImageLoader.Builder(this)
            .allowRgb565(useRgb565)
            .crossfade(if (isLowRam) 0 else 150)
            .memoryCache {
                MemoryCache.Builder(this)
                    .maxSizePercent(if (isLowRam) 0.14 else 0.22)
                    .build()
            }
            .diskCache {
                DiskCache.Builder()
                    .directory(cacheDir.resolve("image_cache"))
                    .maxSizeBytes(if (isLowRam) 32L * 1024 * 1024 else 64L * 1024 * 1024)
                    .build()
            }
            .respectCacheHeaders(false)
            .build()
    }
}
