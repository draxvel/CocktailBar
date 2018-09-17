package com.tkachuk.cocktailbar

import com.squareup.leakcanary.RefWatcher
import com.squareup.leakcanary.LeakCanary
import android.app.Application
import android.content.Context


class MainApplication : Application() {

    private var refWatcher: RefWatcher? = null
    override fun onCreate() {
        super.onCreate()
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return
        }
        refWatcher = LeakCanary.install(this)
        // Normal app init code...
    }

    companion object {

        fun getRefWatcher(context: Context): RefWatcher? {
            val application = context.getApplicationContext() as MainApplication
            return application.refWatcher
        }
    }
}