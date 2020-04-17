package com.example.sale1996.fire_message.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.sale1996.fire_message.R
import com.example.sale1996.fire_message.SignInActivity
import com.example.sale1996.fire_message.glide.GlideApp
import com.example.sale1996.fire_message.util.FirestoreUtil
import com.example.sale1996.fire_message.util.StorageUtil
import com.firebase.ui.auth.AuthUI
import kotlinx.android.synthetic.main.fragment_my_account.*
import kotlinx.android.synthetic.main.fragment_my_account.view.*
import org.jetbrains.anko.clearTask
import org.jetbrains.anko.newTask
import org.jetbrains.anko.support.v4.intentFor
import org.jetbrains.anko.support.v4.toast
import java.io.ByteArrayOutputStream


class MyAccountFragment : Fragment() {

    private val RC_SELECT_IMAGE = 2
    private lateinit var selectedImageBytes: ByteArray
    private var pictureJustChanged = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_my_account, container, false)

        view.apply {
            imageView_profile_picture.setOnClickListener{
                val intent = Intent().apply {
                    type = "image/*"
                    action = Intent.ACTION_GET_CONTENT
                    putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/jpeg", "image/png"))
                }
                startActivityForResult(Intent.createChooser(intent, "Select Image"), RC_SELECT_IMAGE)
            }

            btn_save.setOnClickListener{
                if(::selectedImageBytes.isInitialized){
                    //znaci ako smo postavili sliku...
                    //onda cemo uplodovati sliku na storage util..
                    StorageUtil.uploadProfilePhoto(selectedImageBytes) { imagePath ->
                        //kada smo uspesno ubacili sliku, dobijamo imagePath nazad
                        //pa cemo da sacuvamo korisnika
                        FirestoreUtil.updateCurrentUser(editText_name.text.toString(),
                            editText_bio.text.toString(), imagePath)
                    }
                }
                else{
                    //ako slika nije inicijalizovana, onda samo podatke usera updejtamo
                    FirestoreUtil.updateCurrentUser(editText_name.text.toString(),
                        editText_bio.text.toString(), null)
                }
                toast("Saving")
            }

            //I dodajemo lisener da se izlogujemo lepo
            btn_sign_out.setOnClickListener {
                AuthUI.getInstance()
                    .signOut(requireContext())
                    .addOnCompleteListener {
                        //nakon uspesnog izlogovanja, vracamo se na signIn aktivity i brisemo sve sa steka,
                        //kako ne bi mogli sa back da se vratimo na main screen
                        startActivity(intentFor<SignInActivity>().newTask().clearTask())
                    }
            }

        }

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //data predstavlja intent, a data.data predstavlja sadrzinu intenta
        if(requestCode == RC_SELECT_IMAGE && resultCode == Activity.RESULT_OK &&
                data != null && data.data != null){
            val selectedImagePath = data.data
            val selectedImageBmp = MediaStore.Images.Media
                .getBitmap(activity?.contentResolver, selectedImagePath)

            val outputStream = ByteArrayOutputStream()
            selectedImageBmp.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
            selectedImageBytes = outputStream.toByteArray()

            //TODO: Load picture first time.. onda ne ide iz firebase nego direktno ubacujemo
            GlideApp.with(this)
                .load(selectedImageBytes)
                .into(imageView_profile_picture)

            pictureJustChanged = true
        }
    }

    override fun onStart() {
        super.onStart()
        //ovde cemo loadovati usera iz firestora
        FirestoreUtil.getCurrentUser { user ->
            // nesto na foru kao fragment nece biti vidljiv dok traje povlacenje info iz firebase
            if (this@MyAccountFragment.isVisible){
                editText_name.setText(user.name)
                editText_bio.setText(user.bio)
                if(!pictureJustChanged && user.profilePicturePath != null){
                    //Sada postavljamo sliku sa FIREBASE-a u imageView! Zato nam je trebala ona Glide
                    //klasa!
                    GlideApp.with(this)
                        .load(StorageUtil.pathToReference(user.profilePicturePath)) //zato je pisana metoda ona
                        .placeholder(R.drawable.ic_account_circle_black_24dp)
                        .into(imageView_profile_picture)
                }
            }
        }
    }
}
