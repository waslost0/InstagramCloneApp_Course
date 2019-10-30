package com.example.instagram.activities

import android.os.Bundle
import com.example.instagram.R
import com.example.instagram.utils.CameraHelper

class ShareActivity : BaseActivity(2) {
    override fun getTag(): String {
        return "ShareActivity"
    }
    private lateinit var mCameraHelper: CameraHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share)
        setupBottomNavigation()

        mCameraHelper = CameraHelper(this)
        mCameraHelper.takeCameraPicture()


    }

}
