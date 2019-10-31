package com.example.instagram.utils

import android.app.Activity
import android.net.Uri
import com.example.instagram.activities.showToast
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask

class FirebaseHelper(private val activity: Activity) {

    public val auth: FirebaseAuth =
        FirebaseAuth.getInstance()
    public val database: DatabaseReference = FirebaseDatabase.getInstance()
        .reference
    public val storage: StorageReference = FirebaseStorage.getInstance()
        .reference

    fun uploadUserPhoto(photo: Uri, onSuccess: (UploadTask.TaskSnapshot) -> Unit) {
        storage.child("users/${auth.currentUser!!.uid}/photo").putFile(photo)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    onSuccess(it.result!!)
                } else {
                    activity.showToast(it.exception!!.message!!)
                }
            }
    }

    fun updateUserPhoto(photoUrl: String, onSuccess: () -> Unit) {
        database.child("users/${auth.currentUser!!.uid}/photo").setValue(photoUrl)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    onSuccess()
                } else {
                    activity.showToast(it.exception!!.message!!)
                }
            }
    }

    fun updateUser(update: Map<String, Any?>, onSuccess: () -> Unit) {

        database.child("users").child(auth.currentUser!!.uid).updateChildren(update)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    onSuccess()
                } else {
                    activity.showToast(it.exception!!.message!!)
                }
            }
    }

    fun updateEmail(email: String, onSuccess: () -> Unit) {
        auth.currentUser!!.updateEmail(email).addOnCompleteListener {
            if (it.isSuccessful) {
                onSuccess()
            } else {
                activity.showToast(it.exception!!.message!!)
            }
        }
    }

    fun reauthenticate(credential: AuthCredential, onSuccess: () -> Unit) {
        auth.currentUser!!.reauthenticate(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                onSuccess()
            } else {
                activity.showToast(it.exception!!.message!!)
            }
        }
    }

    fun storageUid() =
        storage.child("users/${auth.currentUser!!.uid}/photo").downloadUrl

    fun storageImgUri(image: String) =
        storage.child("users/${auth.currentUser!!.uid}/images/$image").downloadUrl

    fun currentUserReference(): DatabaseReference =
        database.child("users").child(auth.currentUser!!.uid)
}