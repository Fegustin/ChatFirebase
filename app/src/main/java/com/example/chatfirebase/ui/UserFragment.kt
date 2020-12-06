package com.example.chatfirebase.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.chatfirebase.R
import com.example.chatfirebase.adapter.UserItem
import com.example.chatfirebase.pojo.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.fragment_chat.*


class UserFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fetchUser()
    }

    private fun fetchUser() {
        val ref = Firebase.database.getReference("/users")

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val adapterMessage = GroupAdapter<GroupieViewHolder>()

                dataSnapshot.children.forEach {
                    val user = it.getValue(User::class.java)
                    if (user != null) adapterMessage.add(UserItem(user))
                }

                recyclerViewChat.apply {
                    adapter = adapterMessage
                }

                adapterMessage.setOnItemClickListener { item, _ ->
                    val userItem = item as UserItem
                    val action = UserFragmentDirections.actionChatFragmentToUserChatFragment(userItem.user)
                    findNavController().navigate(action)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("UsersFailed", "Failed to read value.", error.toException())
            }
        })
    }
}