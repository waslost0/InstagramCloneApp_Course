package com.example.instagram

import android.os.Bundle

class HomeActivity : BaseActivity(0) {
    override fun getTag(): String {
        return "HomeActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setupBottomNavigation()

    }

}
