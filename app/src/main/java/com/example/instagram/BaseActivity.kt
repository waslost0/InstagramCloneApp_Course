package com.example.instagram

import android.content.Intent
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.bottom_navigation_view.*

abstract class BaseActivity(val navNumber : Int) : AppCompatActivity(){
    private val TAG = "HomeActivity"

    abstract fun getTag() : String


    fun setupBottomNavigation(){
        bottom_navigation_view.setIconSize(29f)
//        bottom_navigation_view.setTextVisibility(false)
//        bottom_navigation_view.enableAnimation(false)
//        bottom_navigation_view.enableShiftingMode(false)
//        bottom_navigation_view.enableItemShiftingMode(false)

//        bottom_navigation_view.enableAnimation(false)

//        for(i in 0 until bottom_navigation_view.menu.size()){
//            bottom_navigation_view.setIconTintList(i, null)
//        }

        bottom_navigation_view.setOnNavigationItemSelectedListener {
            val NextActivity =
                when(it.itemId)
                {
                    R.id.nav_item_home -> HomeActivity::class.java
                    R.id.nav_item_search -> SearchActivity::class.java
                    R.id.nav_item_share -> ShareActivity::class.java
                    R.id.nav_item_likes -> LikesActivity::class.java
                    R.id.nav_item_profile -> ProfileActivity::class.java
                    else -> {
                        Log.e(TAG, "unknown nav_item_ckicked $it")
                        null
                    }
                }
            if (NextActivity != null) {
                val intent = Intent(this, NextActivity)
                intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
                startActivity(intent)
                overridePendingTransition(0, 0)
                true
            } else {
                false
            }
        }
        bottom_navigation_view.menu.getItem(navNumber).isChecked = true

        Log.d(getTag(), "Activity selected: ${getTag()}")
    }


}
