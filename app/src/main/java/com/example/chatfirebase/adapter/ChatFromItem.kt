package com.example.chatfirebase.adapter

import com.example.chatfirebase.R
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.chat_from_row_layout.*

class ChatFromItem(private val email: String, private val text: String) : Item() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.textViewAuthor.text = email
        viewHolder.textViewMessage.text = text
    }

    override fun getLayout(): Int = R.layout.chat_from_row_layout
}