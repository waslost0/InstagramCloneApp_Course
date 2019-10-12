package com.example.instagram

import android.os.Bundle

class SearchActivity : BaseActivity(1) {
    override fun getTag(): String {
        return "SearchActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        setupBottomNavigation()

    }

}
