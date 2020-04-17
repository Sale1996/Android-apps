package com.example.sale1996.kotlin_messenger.messages

import androidx.appcompat.app.AppCompatActivity
import com.example.sale1996.kotlin_messenger.R
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.example.sale1996.kotlin_messenger.models.ChatMessage
import com.example.sale1996.kotlin_messenger.models.User
import com.example.sale1996.kotlin_messenger.views.ChatFromItem
import com.example.sale1996.kotlin_messenger.views.ChatToItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_chat_log.*
import kotlinx.android.synthetic.main.chat_from_user_row.view.*
import kotlinx.android.synthetic.main.chat_to_user_row.view.*

class ChatLogActivity : AppCompatActivity() {

    val adapter = GroupAdapter<GroupieViewHolder>()
    var toUser : User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)

        recyclerview_chat_log.adapter = adapter

        // uzimamo user objekat iz intenta
        toUser = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
        supportActionBar?.title = toUser?.username

        listenForMessages()

        send_button_chat_log.setOnClickListener {
            performSendMessage()
        }
    }

    private fun listenForMessages(){
        val fromId = FirebaseAuth.getInstance().uid
        val toId = toUser?.uid

        val ref = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId")

        /*
        * Ovaj listener kaze addChild event znaci ukoliko se doda dete cvor u cvoru "/messages",
        * pokrenuce se ovaj lisener
        * */
        ref.addChildEventListener(object: ChildEventListener{
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                // ovde kupimo novododatu poruku
                val chatMessage = p0.getValue(ChatMessage::class.java)

                /*
                * Preko ovoga znamo koji view za row da koristimo (korisnicki ili nas) (levo ili desno poravnan)
                * */
                if(chatMessage != null){
                    if(chatMessage.fromId == FirebaseAuth.getInstance().uid){
                        val currentUser = LatestMessagesActivity.currentUser
                        adapter.add(ChatFromItem(chatMessage.text, currentUser!!))
                    }
                    else{
                        adapter.add(ChatToItem(chatMessage.text, toUser!!))
                    }
                }

                recyclerview_chat_log.scrollToPosition(adapter.itemCount-1)
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
        /*
        * Ovde saljemo poruku do firebase-a
        * Pravimo referencu na novonastali objekat (obrati paznju sa push smo napravili novi objekat)
        * val reference = FirebaseDatabase.getInstance().getReference("/messages").push()
        * */
        val text = edittext_chat_log.text.toString()
        val fromId = FirebaseAuth.getInstance().uid
        val toUser = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
        val toId = toUser.uid
        val systemCurrentTimeSeconds = System.currentTimeMillis()/1000

        if(fromId == null) return //zastita
        /*
        * ovakva organizacija cvorova u !!firebase!! nam daje vecu zastitu za poruke
        * jer onda korisnik moze pokupiti samo podatke koje su za nejga vezani
        * nece praviti upit pa kupiti sve poruke pa gledati koje su njegove
        * */
        val reference = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId").push()
        val referenceForToUser = FirebaseDatabase.getInstance().getReference("/user-messages/$toId/$fromId").push()


        val chatMessage = ChatMessage(reference.key!!, text, fromId, toId, systemCurrentTimeSeconds)
        // insertujemo poruku u firebase za OBA korisnika
        reference.setValue(chatMessage)
            .addOnSuccessListener {
                edittext_chat_log.text.clear()
                recyclerview_chat_log.scrollToPosition(adapter.itemCount-1)
            }
        referenceForToUser.setValue(chatMessage)

        /*
        * Sada zelimo da dodamo zadnju poruku izmedju korisnika u objekat latest_message za OBA
        * korisnika. (ovo je potrebno za funkcionalnost na pocetnom ekranu...)
        * Primetiti da se objekti updejtaju, ne dodavaju novi (jer je samo jedan objekat potreban
        * za oba korisnika, koji je vezan za njihovu komunikaciju)
        * */

        val latestMessageRef = FirebaseDatabase.getInstance().getReference("/latest-messages/$fromId/$toId")
        latestMessageRef.setValue(chatMessage)
        val latestMessageToRef = FirebaseDatabase.getInstance().getReference("/latest-messages/$toId/$fromId")
        latestMessageToRef.setValue(chatMessage)
    }


}


