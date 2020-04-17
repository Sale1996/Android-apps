package com.example.sale1996.kotlin_messenger.registerlogin

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sale1996.kotlin_messenger.messages.LatestMessagesActivity
import com.example.sale1996.kotlin_messenger.R
import com.example.sale1996.kotlin_messenger.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*


class RegisterActivity : AppCompatActivity() {

    var selectedPhotoUri : Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        register_button_register.setOnClickListener {
            performRegister()
        }

        already_have_account_text_view.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        select_photo_button_register.setOnClickListener {
            // preko ovog intenta kazemo daj mi aktivnost koja se bavi pickovanjem slika
            val photoPickIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.type = "image/*"
            startActivityForResult(photoPickIntent, 0)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 0 && resultCode == Activity.RESULT_OK && data != null){
            // ovde preuzimamo podatke o izabranoj slici
            selectedPhotoUri = data.data;
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)
            circle_image_view_register.setImageBitmap(bitmap)

            // sa ovim samo sakrivamo view dugmeta, da bude providno, ali je i dalje tu i dalje je klikabilno
            select_photo_button_register.alpha = 0f
        }
    }

    private fun performRegister(){
        val email = email_edittext_register.text.toString()
        val password = password_edittext_register.text.toString()

        /*  Treba obratiti paznju da ukoliko je neko od polja prazno i pokusamo da se
            registrujemo preko firebase aplikacija ce se srusiti
        */
        if (email.isEmpty() || password.isEmpty()){
            Toast.makeText(this, "Please enter text in email/password!", Toast.LENGTH_SHORT).show()
            return
        }
        // Ovde uzimamo firebase da se registrujemo
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener

                // Sad odvojeno uplodaj sliku koju je korisnik dao
                uploadImageToFirebaseStorage()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to create user: ${it.message}", Toast.LENGTH_LONG).show()
            }
    }

    private fun uploadImageToFirebaseStorage(){
        if(selectedPhotoUri == null) return
        // Sa ovim dobijamo random dugacak string
        val filename = UUID.randomUUID().toString()

        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")

        ref.putFile(selectedPhotoUri!!)
            .addOnSuccessListener {
                Log.d("RegisterActivity", "Successfully uploaded image: ${it.metadata?.path}")

                //pristup lokaciji fajla na firebase-u
                ref.downloadUrl.addOnSuccessListener {
                    Log.d("RegisterActivity", "File Location $it")
                    // Uplodamo klasu User na servis kako bi imali sve info o korisniku
                    saveUserToFirebaseDatabase(it.toString())
                }
            }
            .addOnFailureListener {
                // Ako faila pisemo nesto, po pravilu bi uvek trebalo handlati
            }
    }

    private fun saveUserToFirebaseDatabase(profileImageUrl: String){
        // ako je uid == null onda vrati prazan string
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")

        val user = User(
            uid,
            username_edittext_registration.text.toString(),
            profileImageUrl
        )

        ref.setValue(user)
            .addOnSuccessListener {
                val intent = Intent(this, LatestMessagesActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
            .addOnFailureListener {
                //ako faila pisemo nesto
            }

    }
}


