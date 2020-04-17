package com.example.sale1996.kotlin_messenger.views

import com.example.sale1996.kotlin_messenger.R
import com.example.sale1996.kotlin_messenger.models.User
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.chat_to_user_row.view.*

class ChatToItem(val message : String, val user: User): Item<GroupieViewHolder>(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.text_view_to_user_row.text = message;

        //load our user image into the star
        Picasso.get().load(user.profileImageUrl).into(viewHolder.itemView.image_view_to_user_row)
    }

    override fun getLayout(): Int {
        return R.layout.chat_to_user_row
    }
}