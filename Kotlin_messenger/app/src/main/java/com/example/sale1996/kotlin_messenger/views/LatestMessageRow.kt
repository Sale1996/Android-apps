package com.example.sale1996.kotlin_messenger.views

import com.example.sale1996.kotlin_messenger.R
import com.example.sale1996.kotlin_messenger.models.ChatMessage
import com.example.sale1996.kotlin_messenger.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.latest_message_row.view.*
/*
* Predstavlja jednu prikaz jedne komunikacije sa korisnikom gde je prikazana slicica ime i zadnja poruka
* izmedju njih...
* */
class LatestMessageRow(val chatMessage: ChatMessage): Item<GroupieViewHolder>() {

    var chatPartnerUser: User? = null

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

        viewHolder.itemView.message_textview_latest_message.text = chatMessage.text

        //lets load user information
        val chatPartnerId: String = if(chatMessage.fromId == FirebaseAuth.getInstance().uid){
            chatMessage.toId
        } else {
            chatMessage.fromId
        }
        //preuzimamo iz baze podatke o korisniku
        val ref = FirebaseDatabase.getInstance().getReference("/users/$chatPartnerId")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            // ukoliko korisnik promeni sliku npr. nama ce se automatski promeniti u glavnom prozoru
            override fun onDataChange(p0: DataSnapshot) {
                chatPartnerUser = p0.getValue(User::class.java)

                viewHolder.itemView.username_textview_latest_message.text = chatPartnerUser?.username
                Picasso.get().load(chatPartnerUser?.profileImageUrl).into(viewHolder.itemView.user_imageview_latest_message)
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }

    override fun getLayout(): Int {
        return R.layout.latest_message_row
    }
}