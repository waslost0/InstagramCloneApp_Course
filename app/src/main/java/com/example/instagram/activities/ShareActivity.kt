package com.example.instagram.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.instagram.R
import com.example.instagram.models.FeedPost
import com.example.instagram.models.User
import com.example.instagram.utils.CameraHelper
import com.example.instagram.utils.FirebaseHelper
import com.example.instagram.utils.ValueEventListenerAdapter
import kotlinx.android.synthetic.main.activity_share.*

class ShareActivity : BaseActivity(2) {
    override fun getTag(): String {
        return "ShareActivity"
    }

    private var TAG = "BaseACtivity"

    private lateinit var mCamera: CameraHelper
    private lateinit var mFirebase: FirebaseHelper
    override lateinit var mUser: User


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share)

        mFirebase = FirebaseHelper(this)

        mCamera = CameraHelper(this)
        mCamera.takeCameraPicture()

        back_image.setOnClickListener { finish() }
        share_text.setOnClickListener { share() }
        mFirebase.currentUserReference().addValueEventListener(ValueEventListenerAdapter {
            mUser = it.asUser()!!
        })


    }

    @SuppressLint("MissingSuperCall", "CheckResult")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == mCamera.REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Glide.with(this).load(mCamera.imageUri).centerCrop().into(post_image)
            } else {
                finish()
            }
        }
    }

    private fun share() {
        val imageUri = mCamera.imageUri
        val imgUrl = imageUri?.lastPathSegment.toString()
        finish()

        if (imageUri != null) {
            // upload image to user folder
            val uid = mFirebase.currentUid()!!
            mFirebase.storage.child("users").child(mFirebase.currentUid()!!).child("images")
                .child(imageUri.lastPathSegment.toString()).putFile(imageUri)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        mFirebase.storageImgUri(imgUrl).addOnCompleteListener {
                            val photoUrl = it.result.toString()

                            mFirebase.database.child("images").child(uid).push()
                                .setValue(photoUrl)
                                .addOnCompleteListener {
                                    if (it.isSuccessful) {
                                        mFirebase.database.child("feed-posts").child(uid)
                                            .push()
                                            .setValue(mkFeedPost(uid, photoUrl))
                                            .addOnCompleteListener {
                                                if (it.isSuccessful)
                                                    showToast("Posted")
                                            }
                                    } else {
                                        showToast(it.exception!!.message!!)
                                    }
                                }
                        }
                    } else {
                        showToast(it.exception!!.message!!)
                    }
                }
        }
    }

    private fun mkFeedPost(uid: String, imageDownloadUrl: String): FeedPost {
        return FeedPost(
            uid = uid,
            username = mUser.username,
            image = imageDownloadUrl,
            caption = caption_input.text.toString(),
            photo = mUser.photo
        )
    }
}









