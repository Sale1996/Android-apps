package com.example.sale1996.kotlin_messenger.messages

import androidx.appcompat.app.AppCompatActivity
import com.example.sale1996.kotlin_messenger.R
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.example.sale1996.kotlin_messenger.models.ChatMessage
import com.example.sale1996.kotlin_messenger.models.User
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

        toUser = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
        supportActionBar?.title = toUser?.username

        listenForMessages()

        send_button_chat_log.setOnClickListener {
            //ovde saljemo poruku
            performSendMessage()
        }
    }

    private fun listenForMessages(){
        val fromId = FirebaseAuth.getInstance().uid
        val toId = toUser?.uid
        Log.d("sale-pare", "FromId: $fromId , TOID: $toId")
        val ref = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId")

        //ovaj listener kaze addChild event znaci ukoliko se doda dete cvor u cvoru "/messages"
        //pokrenuce se ovaj lisener
        ref.addChildEventListener(object: ChildEventListener{
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                //ovde kupimo novododatu poruku
                val chatMessage = p0.getValue(ChatMessage::class.java)

                if(chatMessage != null){
                    if(chatMessage.fromId == FirebaseAuth.getInstance().uid){
                        val currentUser = LatestMessagesActivity.currentUser
                        adapter.add(ChatFromItem(chatMessage.text, currentUser!!))
                    }
                    else{
                        adapter.add(ChatToItem(chatMessage.text, toUser!!))
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
//        val reference = FirebaseDatabase.getInstance().getReference("/messages").push()

        val text = edittext_chat_log.text.toString()
        val fromId = FirebaseAuth.getInstance().uid
        val toUser = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
        val toId = toUser.uid
        val systemCurrentTimeSeconds = System.currentTimeMillis()/1000
        // ovakva organizacija cvorova u firebase nam daje vecu zastitu za poruke...
        // jer onda korisnik moze pokupiti samo podatke koje su za nejga vezani
        // nece praviti upit pa kupiti sve poruke pa gledati koje su njegove...
        val reference = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId").push()
        val referenceForToUser = FirebaseDatabase.getInstance().getReference("/user-messages/$toId/$fromId").push()


        if(fromId == null) return //zastita

        val chatMessage = ChatMessage(reference.key!!, text, fromId, toId, systemCurrentTimeSeconds)
        //insertujemo poruku u firebase....
        reference.setValue(chatMessage)
            .addOnSuccessListener {
                //ovde cemo da obrisemo tekst u edit textu kada se posalje poruka
                edittext_chat_log.text.clear()
                // sada ova sledeca linija kaze da skrolamo na sam kraj recyclerviewa kada posaljemo poruku
                recyclerview_chat_log.scrollToPosition(adapter.itemCount-1)
            }
        referenceForToUser.setValue(chatMessage)
    }


}

class ChatFromItem(val message: String, val user: User): Item<GroupieViewHolder>(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.text_view_from_user_row.text = message;

        Picasso.get().load(user.profileImageUrl).into(viewHolder.itemView.image_view_from_user_row)

    }

    override fun getLayout(): Int {
        return R.layout.chat_from_user_row
    }
}
//jedan predstavlja za poruke OD korisnika, druga KA korisniku
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

