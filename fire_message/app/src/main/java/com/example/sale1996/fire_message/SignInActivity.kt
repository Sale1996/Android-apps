package com.example.sale1996.fire_message

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.sale1996.fire_message.service.MyFirebaseInstanceIDService
import com.example.sale1996.fire_message.util.FirestoreUtil
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_sign_in.*
import org.jetbrains.anko.clearTask
import org.jetbrains.anko.design.longSnackbar
import org.jetbrains.anko.indeterminateProgressDialog
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.newTask


class SignInActivity : AppCompatActivity() {

    private val RC_SIGN_IN = 1

    // provajdujemo firebase sa potrebnim podacima o logovanju korisnika (sto mi zelimo da nam se prikaze)
    private val signInProviders =
        listOf(AuthUI.IdpConfig.EmailBuilder()
            .setAllowNewAccounts(true)
            .setRequireName(true)
            .build())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        // ovde pokrecemo taj UI od firebase
        account_sign_in.setOnClickListener {
            val intent = AuthUI.getInstance().createSignInIntentBuilder()
                .setAvailableProviders(signInProviders)
                .setLogo(R.drawable.ic_fire_emoji)
                .build()
            startActivityForResult(intent, RC_SIGN_IN)
        }
    }

    /*
    * IdpResponse je firebase auth interfejs/klasa koja nam omogucuje da izvucemo odgovor firebase-a
    * nakon logovanja
    *
    *
    * */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                val progressDialog = indeterminateProgressDialog("Setting up your account")

                //E sada cemo da pozovemo metodu UTIL-a koji smo napravili i kreirati usera u
                //firebase datasetu
                FirestoreUtil.initCurrentUserIfFirstTime {
                    startActivity(intentFor<MainActivity>().newTask().clearTask())

                    /*
                    * Ovde preuzimamo token od ulogovanog korisnika i prosledjujemo ga nasoj
                    * funkciji sa servisa MyFirebaseInstanceIDService...
                    * */
                    FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener(this) {
                            instanceIdResult ->

                        val userToken = instanceIdResult.token
                        MyFirebaseInstanceIDService.addTokenToFirestore(userToken)
                    }
                    progressDialog.dismiss()
                }
            }
            else if (resultCode == Activity.RESULT_CANCELED) {
                if (response == null) return

                when (response.error?.errorCode) {
                    ErrorCodes.NO_NETWORK ->
                        longSnackbar(constraint_layout_sign_in, "No network!")
                    ErrorCodes.UNKNOWN_ERROR ->
                        longSnackbar(constraint_layout_sign_in, "Unknown error")
                }
            }
        }
    }
}