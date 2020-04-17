package com.example.sale1996.kotlin_messenger.views

import com.example.sale1996.kotlin_messenger.R
import com.example.sale1996.kotlin_messenger.models.User
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.user_row_new_message.view.*

//Ovde definisemo klasu koju ce Groupie adapter koristiti...
class UserItem(val user: User): Item<GroupieViewHolder>() {
    override fun getLayout(): Int {
        //ovde povezujemo layout za nas adapter..
        return R.layout.user_row_new_message
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        //ovde prosledjenog ussera bindamo za elemente layouta koji smo prosledili..
        viewHolder.itemView.username_textview_new_message.text = user.username

        Picasso.get().load(user.profileImageUrl).into(viewHolder.itemView.user_circle_image_view_new_message)
    }

}