package com.example.sale1996.fire_message

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sale1996.fire_message.model.ImageMessage
import com.example.sale1996.fire_message.model.MessageType
import com.example.sale1996.fire_message.model.TextMessage
import com.example.sale1996.fire_message.util.FirestoreUtil
import com.example.sale1996.fire_message.util.StorageUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ListenerRegistration
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import com.xwray.groupie.Section
import kotlinx.android.synthetic.main.activity_chat.*
import org.jetbrains.anko.toast
import java.io.ByteArrayOutputStream
import java.util.*

private const val RC_SELECT_IMAGE = 2

class ChatActivity : AppCompatActivity() {

    private lateinit var currentChannelId: String

    private lateinit var messagesListenerRegistration: ListenerRegistration
    private var shouldInitRecyclerView = true
    private lateinit var messagesSection: Section

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = intent.getStringExtra(AppConstants.USER_NAME)

        val otherUserId = intent.getStringExtra(AppConstants.USER_ID)
        FirestoreUtil.getOrCreateChatChannel(otherUserId){ channelId ->
            currentChannelId = channelId

            messagesListenerRegistration =
                FirestoreUtil.addChatMessagesListener(channelId, this, this::updateRecyclerView)

            //nakon sto je kreiran chat channel dodaj lisener na dugme za send...
            image_view_send.setOnClickListener {
                //kreiraj objekat MESSAGE
                val messageToSend = TextMessage(edit_text_message.text.toString(), Calendar.getInstance().time,
                    FirebaseAuth.getInstance().currentUser!!.uid, MessageType.TEXT)
                //brisemo sadrzaj iz edittext polja
                edit_text_message.setText("")
                FirestoreUtil.sendMessage(messageToSend, channelId)
            }

            //sada ide on Click listener za dugme za slanje slike!
            fab_send_image.setOnClickListener {
                //pravimo intent za chooser
                val intent = Intent().apply{
                    type ="image/*"
                    action = Intent.ACTION_GET_CONTENT
                    putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/jpeg", "image/png"))
                }
                startActivityForResult(Intent.createChooser(intent, "Select image"), RC_SELECT_IMAGE)
            }
        }
    }

    //handlamo prikupljanje slike
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == RC_SELECT_IMAGE && resultCode == Activity.RESULT_OK &&
                data != null && data.data != null){
            val selectedImagePath = data.data
            val selectedImageBmp = MediaStore.Images.Media.getBitmap(contentResolver, selectedImagePath)
            val outputStream = ByteArrayOutputStream()
            selectedImageBmp.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
            val selectedImageBytes = outputStream.toByteArray() //vrv to mora ici u bajtovima na firebase

            StorageUtil.uploadMessageImage(selectedImageBytes){ imagePath ->
                //kada smo uplodali sliku, mozemo da napravimo poruku i posaljemo i nju
                val messageToSend=
                    ImageMessage(imagePath, Calendar.getInstance().time,
                        FirebaseAuth.getInstance().currentUser!!.uid)
                FirestoreUtil.sendMessage(messageToSend, currentChannelId)
            }


        }
    }

    private fun updateRecyclerView(messages: List<Item<GroupieViewHolder>>){
        fun init(){
            recycler_view_messages.apply {
                layoutManager = LinearLayoutManager(this@ChatActivity)
                adapter = GroupAdapter<GroupieViewHolder>().apply {
                    messagesSection = Section(messages)
                    this.add(messagesSection)
                }
            }
            shouldInitRecyclerView = false
        }

        fun updateItems() = messagesSection.update(messages)

        if (shouldInitRecyclerView)
            init()
        else
            updateItems()

        recycler_view_messages.scrollToPosition(recycler_view_messages.adapter!!.itemCount -1)
    }
}
