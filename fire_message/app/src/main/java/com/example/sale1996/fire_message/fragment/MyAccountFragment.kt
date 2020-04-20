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
                // ovo je fora kako mozemo pitati da li je lateinit inicijalizovan
                if(::selectedImageBytes.isInitialized){
                    /*
                    * Ukoliko je nova slika postavljena, cuvamo je na firestore
                    * */
                    StorageUtil.uploadProfilePhoto(selectedImageBytes) { imagePath ->
                        /*
                        * Sa prosledjenim imagePath-om updejtamo korisnikov nalog
                        * da bi imali informaciju koja je njegova sliku u moru slika
                        * */
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

            btn_sign_out.setOnClickListener {
                AuthUI.getInstance()
                    .signOut(requireContext())
                    .addOnCompleteListener {
                        /*
                         * nakon uspesnog izlogovanja, vracamo se na signIn aktivity i brisemo sve sa steka,
                         * kako ne bi mogli sa back da se vratimo na main screen
                         */
                        startActivity(intentFor<SignInActivity>().newTask().clearTask())
                    }
            }

        }

        return view
    }

    /*
    * U ovom onActivityResult ce se hanldati prikupljanjem podataka o slici...
    *
    * */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // data predstavlja intent, a data.data predstavlja sadrzinu intenta
        if(requestCode == RC_SELECT_IMAGE && resultCode == Activity.RESULT_OK &&
                data != null && data.data != null){

            // Obrada slike iz dostiglog intenta
            val selectedImagePath = data.data
            val selectedImageBmp = MediaStore.Images.Media
                .getBitmap(activity?.contentResolver, selectedImagePath)
            val outputStream = ByteArrayOutputStream()
            selectedImageBmp.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
            selectedImageBytes = outputStream.toByteArray()

            //I kad je smenio, daj odma da postavimo u imageView
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
                    /*
                    * Sada postavljamo korisnikovu sliku sa firebase baze.
                    * Da bi to omogucili morali smo napraviti metodu unutar
                    * StorageUtil objekta. Posto nama je potrebna referenca od firestore-a
                    * a nju moramo sami da nadjemo preko profilePicturePath-a koji se nalazi
                    * u user objektu
                    * */
                    GlideApp.with(this)
                        .load(StorageUtil.pathToReference(user.profilePicturePath)) //zato je pisana metoda ona
                        .placeholder(R.drawable.ic_account_circle_black_24dp)
                        .into(imageView_profile_picture)
                }
            }
        }
    }
}
