package com.example.sale1996.kotlin_messenger.messages

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.sale1996.kotlin_messenger.R
import com.example.sale1996.kotlin_messenger.models.User
import com.example.sale1996.kotlin_messenger.views.UserItem
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_new_message.*
import kotlinx.android.synthetic.main.user_row_new_message.view.*

class NewMessageActivity : AppCompatActivity() {

    companion object {
        val USER_KEY = "USER_KEY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)

        supportActionBar?.title = "Select User"

        fetchUsers()

    }

    private fun fetchUsers(){
        // izvlacimo sve korisnike
        val ref = FirebaseDatabase.getInstance().getReference("/users")
        // ovaj listener za razliku od drugih je bolji po pitanju performansi..
        ref.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                // pravimo adapter preko nase 3rd party biblioteke
                val adapter = GroupAdapter<GroupieViewHolder>()

                p0.children.forEach{
                    // Samo ovo prosledimo i on ga sam konvertuje u prosledjeni tip
                    // Zbog ovoga smo pravili prazan konstruktor u klasi User
                    val user = it.getValue(User::class.java)
                    if(user != null){
                        adapter.add(UserItem(user))
                    }
                }

                // Hajde da dodamo item click lisener na nas adapter za svaki item
                adapter.setOnItemClickListener { item, view ->
                    // Ovde ne mozemo odma this, nego moramo preko view-a pristupiti nasem activitiju
                    val intent = Intent(view.context, ChatLogActivity::class.java)

                    val userItem = item as UserItem
                    intent.putExtra(USER_KEY, userItem.user)

                    startActivity(intent)

                    finish()
                    /*
                    * Kada se zavrsi aktivnost koju smo startovali "ChatLogActivity", sa ovim smo rekli cim dodje ovde
                    * finish() on umesto da prikaze ovu aktivnost prikazace pocetni prozor...
                    *  */
                }

                recyclerview_newmessage.adapter = adapter
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }

}


