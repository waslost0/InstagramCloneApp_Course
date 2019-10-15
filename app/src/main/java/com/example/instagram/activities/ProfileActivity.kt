package com.example.instagram.activities

import android.content.Intent
import android.os.Bundle
import com.example.instagram.R
import kotlinx.android.synthetic.main.activity_profile.*

//private val access_token = "19750768356.e06740e.f8f0d1c54cfe444b877c416c073d1c8d"

class ProfileActivity : BaseActivity(4) {
    override fun getTag(): String {
        return "ProfileActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        setupBottomNavigation()

        posts_count_text.text = "666"

        edit_profile_btn.setOnClickListener {
            val intent = Intent(this, EditProfileActivity::class.java)
            startActivity(intent)
        }

    }

}
