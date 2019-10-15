package com.example.instagram.activities

import android.os.Bundle
import com.example.instagram.R

class ShareActivity : BaseActivity(2) {
    override fun getTag(): String {
        return "ShareActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setupBottomNavigation()
    }

}
