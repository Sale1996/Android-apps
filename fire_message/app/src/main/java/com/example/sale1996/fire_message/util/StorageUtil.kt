package com.example.sale1996.fire_message.util

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.*

object StorageUtil {

    private val storageInstance: FirebaseStorage by lazy { FirebaseStorage.getInstance() }

    //sad instanciramo cvor..
    private val currentUserRef: StorageReference
        get() = storageInstance.reference
            .child(FirebaseAuth.getInstance().uid ?: throw NullPointerException("UDI is null."))

    fun uploadProfilePhoto(imageBytes: ByteArray,
                           onSuccess: (imagePath: String) -> Unit){
        val ref = currentUserRef.child("profilePictures/${UUID.nameUUIDFromBytes(imageBytes)}")
        // postavljamo sliku
        ref.putBytes(imageBytes)
            .addOnSuccessListener {
                onSuccess(ref.path)
            }
    }

    fun uploadMessageImage(imageBytes: ByteArray,
                           onSuccess: (imagePath: String) -> Unit){
        val ref = currentUserRef.child("messages/${UUID.nameUUIDFromBytes(imageBytes)}")
        // postavljamo sliku
        ref.putBytes(imageBytes)
            .addOnSuccessListener {
                onSuccess(ref.path)
            }
    }

    fun pathToReference(path: String) = storageInstance.getReference(path);
}