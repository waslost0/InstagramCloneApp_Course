package com.example.instagram

import android.os.Bundle

class ShareActivity : BaseActivity(2) {
    override fun getTag(): String {
        return "ShareActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        setupBottomNavigation()
    }

}
