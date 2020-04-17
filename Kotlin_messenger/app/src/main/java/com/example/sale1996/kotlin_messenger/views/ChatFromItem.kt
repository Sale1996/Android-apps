package com.example.sale1996.kotlin_messenger.views

import com.example.sale1996.kotlin_messenger.R
import com.example.sale1996.kotlin_messenger.models.User
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.chat_from_user_row.view.*

class ChatFromItem(val message: String, val user: User): Item<GroupieViewHolder>(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.text_view_from_user_row.text = message;

        Picasso.get().load(user.profileImageUrl).into(viewHolder.itemView.image_view_from_user_row)

    }

    override fun getLayout(): Int {
        return R.layout.chat_from_user_row
    }
}