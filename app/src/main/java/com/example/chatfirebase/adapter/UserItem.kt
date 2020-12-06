package com.example.chatfirebase.adapter

import com.example.chatfirebase.R
import com.example.chatfirebase.pojo.User
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.user_list_layout.*

class UserItem(val user: User) : Item() {

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.textViewUser.text = user.email
    }

    override fun getLayout(): Int = R.layout.user_list_layout
}