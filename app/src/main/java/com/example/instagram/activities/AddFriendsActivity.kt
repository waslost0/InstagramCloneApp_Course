package com.example.instagram.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.instagram.R
import com.example.instagram.models.User
import com.example.instagram.utils.FirebaseHelper
import com.example.instagram.utils.TaskSourceOnCompleteListener
import com.example.instagram.utils.ValueEventListenerAdapter
import com.google.android.gms.tasks.Tasks
import com.google.firebase.database.DatabaseReference
import kotlinx.android.synthetic.main.activity_add_friends.*
import kotlinx.android.synthetic.main.add_friends_item.view.*

class AddFriendsActivity : AppCompatActivity(), FriendsAdapter.Listener {

    private lateinit var mAdapter: FriendsAdapter

    private lateinit var mUsers: List<User>

    private lateinit var mUser: User
    private lateinit var mFirebase: FirebaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_friends)

        mFirebase = FirebaseHelper(this)
        mAdapter = FriendsAdapter(this)

        val uid = mFirebase.currentUid()!!

        add_friends_recycler.adapter = mAdapter
        add_friends_recycler.layoutManager = LinearLayoutManager(this)
        back_image.setOnClickListener { finish() }

        mFirebase.database.child("users").addValueEventListener(ValueEventListenerAdapter { it ->
            val allUsers = it.children.map { it.asUser()!! }
            val (userList, otherUsersList) = allUsers.partition { it.uid == uid }
            mUser = userList.first()
            mUsers = otherUsersList

            mAdapter.update(mUsers, mUser.follows)
        })

    }


    override fun follow(uid: String) {
        setFollow(uid, true) {
            mAdapter.followed(uid)
        }
    }

    override fun unfollow(uid: String) {
        setFollow(uid, false) {
            mAdapter.unfollowed(uid)
        }
    }

    private fun setFollow(uid: String, follow: Boolean, onSuccess: () -> Unit) {
        // /users/mUser.uid/follows/uid -> true
        // /users/uid/followers/mUser.uid -> true


        val followsTask =
            mFirebase.database.child("users").child(mUser.uid).child("follows")
                .child(uid).setValueTrueOrRemove(follow)
        val followersTask = mFirebase.database.child("users").child(uid).child("followers")
            .child(mUser.uid).setValueTrueOrRemove(follow)


        val feedPostsTask = task<Void> { taskSource ->

            mFirebase.database.child("feed-posts").child(uid)
                .addListenerForSingleValueEvent(ValueEventListenerAdapter { it ->
                    val postsMap = if (follow) {
                        it.children.map { it.key to it.value }.toMap()
                    } else {
                        it.children.map { it.key to null }.toMap()
                    }
                    mFirebase.database.child("feed-posts").child(mUser.uid)
                        .updateChildren(postsMap)
                        .addOnCompleteListener(
                            TaskSourceOnCompleteListener(
                                taskSource
                            )
                        )
                })
        }
        Tasks.whenAll(followsTask, followersTask, feedPostsTask)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    onSuccess()
                } else {
                    showToast(it.exception!!.message!!)
                }
            }
    }
}



class FriendsAdapter(private val listener: Listener) :
    RecyclerView.Adapter<FriendsAdapter.ViewHolder>() {

    private var mPosition = mapOf<String, Int>()
    private var mUsers = listOf<User>()
    private var mFollows = mapOf<String, Boolean>()

    interface Listener {
        fun follow(uid: String)
        fun unfollow(uid: String)
    }

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.add_friends_item, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            val user = mUsers[position]

            view.photo_image.loadUserPhoto(user.photo)
            view.username_text.text = user.username
            view.username_text.text = user.name
            view.follow_button.setOnClickListener { listener.follow(user.uid) }
            view.unfollow_button.setOnClickListener { listener.unfollow(user.uid) }

            val follows = mFollows[user.uid] ?: false
            if (follows) {
                view.follow_button.visibility = View.GONE
                view.unfollow_button.visibility = View.VISIBLE
            } else {
                view.follow_button.visibility = View.VISIBLE
                view.unfollow_button.visibility = View.GONE
            }
        }
    }


    override fun getItemCount(): Int = mUsers.size
    fun update(users: List<User>, follows: Map<String, Boolean>) {
        mUsers = users
        mPosition = users.withIndex().map { (idx, user) -> user.uid to idx }.toMap()

        mFollows = follows
        notifyDataSetChanged()
    }

    fun followed(uid: String) {
        mFollows = mFollows + (uid to true)
        notifyItemChanged(mPosition[uid]!!)
    }

    fun unfollowed(uid: String) {
        mFollows = mFollows - uid
        notifyItemChanged(mPosition[uid]!!)
    }
}