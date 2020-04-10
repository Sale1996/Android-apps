package com.example.sale1996.kotlin_messenger.messages

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.sale1996.kotlin_messenger.R
import com.example.sale1996.kotlin_messenger.models.User
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)

        supportActionBar?.title = "Select User"

        fetchUsers()

        //pravimo adapter preko nase 3rd party biblioteke...
//        val adapter = GroupAdapter<GroupieViewHolder>()
//
//        recyclerview_newmessage.adapter = adapter
    }

    companion object {
        val USER_KEY = "USER_KEY"
    }

    private fun fetchUsers(){
        //kupimo sve usere
        val ref = FirebaseDatabase.getInstance().getReference("/users")
        // ovaj listener za razliku od drugih je bolji po pitanju performansi..
        ref.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                //iz nekog razloga pravi opet adapter
                val adapter = GroupAdapter<GroupieViewHolder>()


                p0.children.forEach{
                    val user = it.getValue(User::class.java) //samo ovo prosledimo i on ga sam konvertuje u prosledjeni tip
                    if(user != null){
                        adapter.add(UserItem(user))
                    }
                }

                //hajde da dodamo item click lisener na nas adapter za svaki item
                adapter.setOnItemClickListener { item, view ->
                    // ovde ne mozemo odma this, nego moramo preko view-a pristupiti nasem activitiju
                    val intent = Intent(view.context, ChatLogActivity::class.java)
                    // ovde cemo da prosledjujemo korisnika koga smo kliknuli
                    val userItem = item as UserItem //to je kliknuto...
                    intent.putExtra(USER_KEY, userItem.user)

                    startActivity(intent)

                    finish() //sa ovom linijom ako odemo back od onog chat-a on automatski zavrsava
                    //i sa ovom aktivnoscu, pa se vraca na main prozor odnosno naredni back...
                }

                recyclerview_newmessage.adapter = adapter
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }

}

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
