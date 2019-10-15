package com.example.instagram.activities

import android.os.Bundle
import com.example.instagram.R

class SearchActivity : BaseActivity(1) {
    override fun getTag(): String {
        return "SearchActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setupBottomNavigation()

    }

}
