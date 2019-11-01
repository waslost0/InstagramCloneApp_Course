package com.example.instagram.activities

import android.content.Intent
import android.os.Bundle
import com.example.instagram.R
import com.example.instagram.utils.FirebaseHelper
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_likes.*

class LikesActivity : BaseActivity(3) {
    override fun getTag(): String {
        return "LikesActivity"
    }
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_likes)
        setupBottomNavigation()

        mAuth = FirebaseAuth.getInstance()
        sign_out_text.setOnClickListener{
            mAuth.signOut()
        }

        mAuth.addAuthStateListener {
            if(it.currentUser == null){
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
    }




}
