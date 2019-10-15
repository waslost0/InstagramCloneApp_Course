package com.example.instagram.activities

import android.os.Bundle
import com.example.instagram.R

class LikesActivity : BaseActivity(3) {
    override fun getTag(): String {
        return "LikesActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setupBottomNavigation()
    }

}
