package com.example.chatfirebase

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.chatfirebase.adapter.ChatFromItem
import com.example.chatfirebase.adapter.ChatToItem
import com.example.chatfirebase.pojo.Message
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.fragment_user_chat.*


class UserChatFragment : Fragment() {

    private val argsLazy by navArgs<UserChatFragmentArgs>()

    private val adapterMessage = GroupAdapter<GroupieViewHolder>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_user_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rVChat.adapter = adapterMessage

        listenerMessages()

        buttonSend.setOnClickListener {
            sendMessage()
        }
    }

    private fun listenerMessages() {
        val fromId = FirebaseAuth.getInstance().uid
        val toId = argsLazy.user.uid

        val ref = Firebase.database.getReference("/user-messages/$fromId/$toId")
        val currentUser = FirebaseAuth.getInstance().currentUser

        ref.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chatMessage = snapshot.getValue(Message::class.java)

                if (chatMessage != null) {
                    if (chatMessage.fromId != FirebaseAuth.getInstance().uid) {
                        adapterMessage.add(ChatFromItem(argsLazy.user.email, chatMessage.message))
                    } else {
                        currentUser?.email?.let { ChatToItem(it, chatMessage.message) }?.let {
                            adapterMessage.add(
                                it
                            )
                        }
                    }
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildRemoved(snapshot: DataSnapshot) {

            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    private fun sendMessage() {
        val fromId = FirebaseAuth.getInstance().uid
        val toId = argsLazy.user.uid

//        val ref = Firebase.database.getReference("/messages").push()
        val ref = Firebase.database.getReference("/user-messages/$fromId/$toId").push()
        val toRef = Firebase.database.getReference("/user-messages/$toId/$fromId").push()
        val message = Message(
            ref.key.toString(),
            editTextMessage.text.trim().toString(),
            fromId.toString(),
            toId
        )

        ref.setValue(message)
            .addOnSuccessListener {
                editTextMessage.text.clear()
                rVChat.scrollToPosition(adapterMessage.itemCount - 1)
            }
        toRef.setValue(message)
    }
}