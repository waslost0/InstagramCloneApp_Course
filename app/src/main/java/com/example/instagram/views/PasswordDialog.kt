package com.example.instagram.views

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.instagram.R
import kotlinx.android.synthetic.main.dialog_password.view.*

class PasswordDialog : DialogFragment(){
    private lateinit var mListener: Listener
    interface Listener {
        fun onPasswordConfirm(password : String)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mListener = context as Listener
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = activity!!.layoutInflater.inflate(R.layout.dialog_password, null)
        return AlertDialog.Builder(context!!)
            .setPositiveButton(android.R.string.ok) { _, _ ->
                mListener.onPasswordConfirm(view.password_input.text.toString())
            }
            .setNegativeButton(android.R.string.cancel) { _, _ ->
                //donothing
            }
            .setTitle(R.string.please_enter_password)
            .setView(view).create()
    }
}