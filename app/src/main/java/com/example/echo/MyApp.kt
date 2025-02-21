package com.example.echo

import android.app.Application
import android.util.Log
import com.parse.Parse

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()

        Parse.initialize(
            Parse.Configuration.Builder(this)
                .applicationId("PH4mFZPFlFrXcRmq0ZOXLCKxM7AlZchKBIHPMnZS")
                .clientKey("zWLfzXLNiqkEbq5ENlLdqc0e8xR8IxgFs1BCSXeB")
                .server("https://parseapi.back4app.com")
                .build()
        )

        Log.d("ParseInit", "Parse initialized successfully!") //
    }
}