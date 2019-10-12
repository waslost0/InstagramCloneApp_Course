package com.example.instagram

import android.os.Bundle

class MainActivity : BaseActivity(0) {
    override fun getTag(): String {
        return "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        setupBottomNavigation()

    }

}
