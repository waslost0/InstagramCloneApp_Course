package com.example.instagram

import android.os.Bundle

class LikesActivity : BaseActivity(3) {
    override fun getTag(): String {
        return "LikesActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        setupBottomNavigation()
    }

}
