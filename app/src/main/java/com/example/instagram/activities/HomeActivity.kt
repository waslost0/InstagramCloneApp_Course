package com.example.instagram.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.instagram.R
import com.example.instagram.utils.FirebaseHelper
import com.example.instagram.utils.ValueEventListenerAdapter
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.feed_item.view.*

class HomeActivity : BaseActivity(0) {
    override fun getTag(): String {
        return "HomeActivity"
    }

    private val TAG = "HomeActivity"
    private lateinit var mFirebase: FirebaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setupBottomNavigation()

        mFirebase = FirebaseHelper(this)

        mFirebase.auth.addAuthStateListener {
            if (it.currentUser == null) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }

    }

    override fun onStart() {
        super.onStart()
        val currentUser = mFirebase.auth.currentUser
        if (currentUser == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        } else {
            mFirebase.database.child("feed-posts").child(currentUser.uid)
                .addValueEventListener((ValueEventListenerAdapter {
                    val posts = it.children.map { it.getValue(FeedPost::class.java)!! }
                    Log.d(TAG, "feedPosts: ${posts.first().timestampDate()}")
                    feed_recycler.adapter = FeedAdapter(posts)
                    feed_recycler.layoutManager = LinearLayoutManager(this)

                }))
        }
    }

}


class FeedAdapter(private val posts: List<FeedPost>) :
    RecyclerView.Adapter<FeedAdapter.ViewHolder>() {

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder{
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.feed_item, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.view.post_image.loadImage(posts[position].image)

    }

    private fun ImageView.loadImage(image: String){
        Glide.with(this).load(image).centerCrop().into(this)
    }

    override fun getItemCount(): Int = posts.size

}
