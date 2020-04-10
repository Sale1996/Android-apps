package com.example.sale1996.kotlin_messenger.messages

import androidx.appcompat.app.AppCompatActivity
import com.example.sale1996.kotlin_messenger.R
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.example.sale1996.kotlin_messenger.models.ChatMessage
import com.example.sale1996.kotlin_messenger.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_chat_log.*
import kotlinx.android.synthetic.main.chat_from_user_row.view.*
import kotlinx.android.synthetic.main.chat_to_user_row.view.*

class ChatLogActivity : AppCompatActivity() {

    val adapter = GroupAdapter<GroupieViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)

        recyclerview_chat_log.adapter = adapter

        val user = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
        supportActionBar?.title = user.username

        listenForMessages()

        send_button_chat_log.setOnClickListener {
            //ovde saljemo poruku
            performSendMessage()
        }
    }

    private fun listenForMessages(){
        val ref = FirebaseDatabase.getInstance().getReference("/messages")

        //ovaj listener kaze addChild event znaci ukoliko se doda dete cvor u cvoru "/messages"
        //pokrenuce se ovaj lisener
        ref.addChildEventListener(object: ChildEventListener{
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                //ovde kupimo novododatu poruku
                val chatMessage = p0.getValue(ChatMessage::class.java)

                if(chatMessage != null){
                    if(chatMessage.fromId == FirebaseAuth.getInstance().uid){
                        adapter.add(ChatFromItem(chatMessage.text))
                    }
                    else{
                        adapter.add(ChatToItem(chatMessage.text))
                    }
                }
            }

            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildRemoved(p0: DataSnapshot) {
            }
        })

    }

    private fun performSendMessage(){
        //ovde saljemo poruku do firebase-a
        //ovde pravimo referencu na novonastali objekat (obrati paznju sa push smo napravili novi objekat)
        val reference = FirebaseDatabase.getInstance().getReference("/messages").push()

        val text = edittext_chat_log.text.toString()
        val fromId = FirebaseAuth.getInstance().uid
        val toUser = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
        val toId = toUser.uid
        val systemCurrentTimeSeconds = System.currentTimeMillis()/1000

        if(fromId == null) return //zastita

        val chatMessage = ChatMessage(reference.key!!, text, fromId, toId, systemCurrentTimeSeconds)
        //insertujemo poruku u firebase....
        reference.setValue(chatMessage)
            .addOnSuccessListener {
                //stogod
            }
    }

//    private fun setupDummyData(){
//        val adapter = GroupAdapter<GroupieViewHolder>()
//
//        adapter.add(ChatFromItem())
//        adapter.add(ChatToItem())
//        adapter.add(ChatFromItem())
//        adapter.add(ChatToItem())
//        adapter.add(ChatFromItem())
//        adapter.add(ChatToItem())
//        adapter.add(ChatFromItem())
//        adapter.add(ChatToItem())
//        adapter.add(ChatFromItem())
//        adapter.add(ChatToItem())
//        adapter.add(ChatFromItem())
//        adapter.add(ChatToItem())
//
//        recyclerview_chat_log.adapter = adapter
//    }
}

class ChatFromItem(val message: String): Item<GroupieViewHolder>(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.text_view_from_user_row.text = message;
    }

    override fun getLayout(): Int {
        return R.layout.chat_from_user_row
    }
}
//jedan predstavlja za poruke OD korisnika, druga KA korisniku
class ChatToItem(val message : String): Item<GroupieViewHolder>(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.text_view_to_user_row.text = message;

    }

    override fun getLayout(): Int {
        return R.layout.chat_to_user_row
    }
}

