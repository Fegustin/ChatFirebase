package com.example.chatfirebase.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.chatfirebase.R
import com.example.chatfirebase.pojo.User
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_auth.*


class AuthFragment : Fragment() {

    private val RC_SIGN_IN = 1001

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_auth, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
        )

        buttonToChat.setOnClickListener {
            startActivityForResult(
                AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .build(),
                RC_SIGN_IN
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
//            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                // Save User in Database
                val user = FirebaseAuth.getInstance().currentUser

                if (user != null) {

                    val uid = user.uid
                    val email = user.email
                    val ref = Firebase.database.getReference("/users/$uid")

                    ref.setValue(email?.let { User(uid, it) })
                        .addOnSuccessListener {
                            // Navigation
                            val action = AuthFragmentDirections.actionAuthFragmentToChatFragment()
                            findNavController().navigate(action)
                        }
                }
            } else {
                Toast.makeText(activity, "Случилась хуйня", Toast.LENGTH_SHORT).show()
            }
        }
    }
}