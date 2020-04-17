package com.example.sale1996.fire_message.recyclerview.item

import android.content.Context
import com.example.sale1996.fire_message.R
import com.example.sale1996.fire_message.glide.GlideApp
import com.example.sale1996.fire_message.model.User
import com.example.sale1996.fire_message.util.StorageUtil
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.item_person.view.*

class PersonItem(val person: User,
                 val userId: String,
                 private val context: Context
)
    : Item<GroupieViewHolder>(){
    override fun getLayout(): Int  = R.layout.item_person


    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.textView_name.text = person.name
        viewHolder.itemView.textView_bio.text = person.bio
        if(person.profilePicturePath != null){
            GlideApp.with(context)
                .load(StorageUtil.pathToReference(person.profilePicturePath))
                .placeholder(R.drawable.ic_account_circle_black_24dp)
                .into(viewHolder.itemView.imageView_profile_picture)
        }
    }
}