package com.example.instagram.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.instagram.R
import com.example.instagram.models.User
import com.example.instagram.utils.CameraHelper
import com.example.instagram.utils.FirebaseHelper
import com.example.instagram.utils.ValueEventListenerAdapter
import com.example.instagram.views.PasswordDialog
import com.google.firebase.auth.EmailAuthProvider
import kotlinx.android.synthetic.main.activity_edit_profile.*


class EditProfileActivity : AppCompatActivity(), PasswordDialog.Listener {

    private val TAG = "EditProfileActivity"
    private lateinit var mUser: User
    private lateinit var mPendingUser: User
    private lateinit var mFirebaseHelper: FirebaseHelper
    private lateinit var cameraHelper: CameraHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        Log.d(TAG, "onCreate")

        cameraHelper = CameraHelper(this)
        close_image.setOnClickListener { finish() }
        save_image.setOnClickListener { updateProfile() }
        change_photo_text.setOnClickListener { cameraHelper.takeCameraPicture() }


        mFirebaseHelper = FirebaseHelper(this)

        mFirebaseHelper.currentUserReference()
            .addListenerForSingleValueEvent(ValueEventListenerAdapter {
                mUser = it.getValue(User::class.java)!!
                name_input.setText(mUser.name)
                username_input.setText(mUser.username)
                email_input.setText(mUser.email)
                website_input.setText(mUser.website)
                phone_input.setText(mUser.phone?.toString())
                bio_input.setText(mUser.bio)
                profile_image.loadUserPhoto(mUser.photo)

            })
    }


    @SuppressLint("MissingSuperCall")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == cameraHelper.REQUEST_CODE && resultCode == RESULT_OK) {
            mFirebaseHelper.uploadUserPhoto(cameraHelper.imageUri!!)
            {
                mFirebaseHelper.storageUid().addOnCompleteListener {
                    val photoUrl = it.result.toString()
                    mFirebaseHelper.updateUserPhoto(photoUrl) {
                        mUser = mUser.copy(photo = photoUrl)
                        profile_image.loadUserPhoto(mUser.photo)
                    }
                }
            }

        }
    }


    private fun updateProfile() {
        mPendingUser = readInputs()

        val error = validateUser(mPendingUser)
        if (error == null) {
            if (mPendingUser.email == mUser.email) {
                updateUser(mPendingUser)
            } else {
                PasswordDialog().show(supportFragmentManager, "password_dialog")
            }
        } else {
            showToast(error)
        }
    }

    private fun readInputs(): User {
        return User(
            name = name_input.text.toString(),
            email = email_input.text.toString(),
            username = username_input.text.toString(),
            website = website_input.text.toStringOrNull(),
            bio = bio_input.text.toStringOrNull(),
            phone = phone_input.text.toString().toLongOrNull()
        )
    }


    private fun updateUser(user: User) {
        val updatesMap = mutableMapOf<String, Any?>()
        if (user.name != mUser.name) updatesMap["name"] = user.name
        if (user.username != mUser.username) updatesMap["username"] = user.username
        if (user.website != mUser.website) updatesMap["website"] = user.website
        if (user.bio != mUser.bio) updatesMap["bio"] = user.bio
        if (user.email != mUser.email) updatesMap["email"] = user.email
        if (user.phone != mUser.phone) updatesMap["phone"] = user.phone

        mFirebaseHelper.updateUser(updatesMap) {
            showToast("Profile saved")
            finish()
        }
    }


    private fun validateUser(user: User): String? =
        when {
            user.name.isEmpty() -> "Please enter name"
            user.username.isEmpty() -> "Please enter username"
            user.email.isEmpty() -> "Please enter name"
            else -> null
        }

    override fun onPasswordConfirm(password: String) {
        if (password.isNotEmpty()) {
            val credential = EmailAuthProvider.getCredential(mUser.email, password)
            mFirebaseHelper.reauthenticate(credential) {
                mFirebaseHelper.updateEmail(mPendingUser.email) {
                    updateUser(mPendingUser)
                }
            }
        } else {
            showToast("Please enter the password")
        }
    }

}

