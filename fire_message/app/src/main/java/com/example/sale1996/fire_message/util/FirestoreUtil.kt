package com.example.sale1996.fire_message.util

import android.content.Context
import android.util.Log
import com.example.sale1996.fire_message.model.*
import com.example.sale1996.fire_message.recyclerview.item.ImageMessageItem
import com.example.sale1996.fire_message.recyclerview.item.PersonItem
import com.example.sale1996.fire_message.recyclerview.item.TextMessageItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import java.lang.NullPointerException

object FirestoreUtil {
    // lazy znaci da ce se instancirati, SAMO kada budemo izvlacili instancu
    private val firestoreInstance: FirebaseFirestore by lazy { FirebaseFirestore.getInstance()}

    // Unit je zapravo void kao u javi
    // Napravljen je objekat currentUserDocRef, i kada se pozove geter, overridovali smo tu metodu
    // da vrati ono sto mi zelimo, a to je usera, ukoliko je uid null, baci exception
    // Ovo nam samo predstavlja cvor od naseg korisnika..
    private val currentUserDocRef: DocumentReference
        get() = firestoreInstance.document("users/${FirebaseAuth.getInstance().uid
            ?: throw NullPointerException("UID is null.")}")

    // ovde imamo referencu na sve chatove korisnika...
    private val chatChannelsCollectionRef = firestoreInstance.collection("chatChannels")

    //Kao parametar prima funkciju onComplete koja kao povratnu vrednost vraca void (Unit)
    fun initCurrentUserIfFirstTime(onComplete: () -> Unit){
        currentUserDocRef.get().addOnSuccessListener { documentSnapshot ->
            // Ako korisnika nema u bazi onda ga dodaj
            if(!documentSnapshot.exists()){
                val newUser = User(FirebaseAuth.getInstance().currentUser?.displayName ?: "",
                    "",null, mutableListOf())

                currentUserDocRef.set(newUser).addOnSuccessListener {
                    // Korisnik uspeesno dodat !!
                    onComplete()
                }
            }
            else{
                onComplete()
            }
        }
    }

    fun updateCurrentUser(name: String = "",bio: String ="", profilePicturePath: String? = null){
        val userFiledMap = mutableMapOf<String, Any>()
        if (name.isNotBlank()) userFiledMap["name"] = name
        if (bio.isNotBlank()) userFiledMap["bio"] = bio
        if (profilePicturePath != null) userFiledMap["profilePicturePath"] = profilePicturePath

        // Ovo je postavljeno, jer mi ne moramo uvek celog korisnika izmeniti, nego samo odredjena polja...
        currentUserDocRef.update(userFiledMap)
    }

    fun getCurrentUser(onComplete: (User) -> Unit){
        currentUserDocRef.get()
            .addOnSuccessListener {
                //ovo nam govori kad god se objekat promeni da se poziva ova funkcija opet...
                onComplete(it.toObject(User::class.java)!!)
            }
    }

    /*
    *
    * Kada pokupi sve korisnike iz firebase-a, onda samo pozove funkciju koju smo prosledili
    * a ta funkcija je funkcija npr iz peoplefragmenta updateRecyclerView...
    *
    *  */
    fun addUsersListener(context: Context, onListen: (List<Item<GroupieViewHolder>>)-> Unit): ListenerRegistration {
        return firestoreInstance.collection("users")
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if(firebaseFirestoreException != null){
                    Log.e("FIRESTORE", "Users listener error.", firebaseFirestoreException)
                    return@addSnapshotListener
                }

                val items = mutableListOf<Item<GroupieViewHolder>>()
                querySnapshot?.documents?.forEach {
                    if (it.id != FirebaseAuth.getInstance().currentUser?.uid)
                        items.add(PersonItem(it.toObject(User::class.java)!!, it.id, context))
                }
                onListen(items)
            }
    }

    fun removeListener(registration: ListenerRegistration) = registration.remove()

    //ova funkcija kao rezultat poziva onComplete funkciju i prosledjuje joj channelId
    fun getOrCreateChatChannel(otherUserId: String,
                               onComplete: (channelId: String) -> Unit){
        currentUserDocRef.collection("engagedChatChannels")
            .document(otherUserId).get().addOnSuccessListener {
                if(it.exists()){
                    //ukoliko vec postoji chat kanal izmedju logovanog korisnika i otherUserId-a
                    onComplete(it["channelId"] as String)
                    return@addOnSuccessListener
                }

                //ako ne postoji, vracamo nazad...
                val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid

                // prvo pravimo channel, pa kada dobijemo ID od njega, ubacujemo u cvor korisnika
                // kako bismo ga mogli naci kasnije
                val newChannel = chatChannelsCollectionRef.document()
                newChannel.set(ChatChannel(mutableListOf(currentUserId, otherUserId)))

                currentUserDocRef
                    .collection("engagedChatChannels")
                    .document(otherUserId)
                    .set(mapOf("channelId" to newChannel.id)) // ovde smestamo informaciju o Id channela, u mapu sa kljucem "channelId"

                //Ali posto chetujemo sa drugim korisnicima, moramo da unutar engagedChatChannels postavimo i njima
                //informaciju o ovom chat channelu

                firestoreInstance.collection("users").document(otherUserId)
                    .collection("engagedChatChannels")
                    .document(currentUserId)
                    .set(mapOf("channelId" to newChannel.id))

                onComplete(newChannel.id)
            }
    }

    // pravi se lisener na chatmessages
    fun addChatMessagesListener(channelId: String, context: Context,
                                onListen: (List<Item<GroupieViewHolder>>) -> Unit): ListenerRegistration{
        // time  je time od objekta kako bi ga mogli sortirati
        return chatChannelsCollectionRef.document(channelId).collection("messages")
            .orderBy("time")
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if (firebaseFirestoreException != null){
                    Log.e("FIRESTORE", "ChatMessagesListener error.", firebaseFirestoreException)
                    return@addSnapshotListener
                }

                val items = mutableListOf<Item<GroupieViewHolder>>()
                querySnapshot?.documents?.forEach {
                    if(it["type"] == MessageType.TEXT){
                        items.add(TextMessageItem(it.toObject(TextMessage::class.java)!!, context))
                    }
                    else{
                        items.add(ImageMessageItem(it.toObject(ImageMessage::class.java)!!, context))
                    }
                }
                onListen(items) //pozivamo onu funkciju koju smo prosledili
            }
    }

    //firebase funkcija za slanje poruka
    fun sendMessage(message: Message, channelId: String){
        chatChannelsCollectionRef.document(channelId)
            .collection("messages")
            .add(message)
    }

    //Firestore chat messenger setup
    //region FCM, uzimamo sve tokene od ulogovanog korisnika...
    fun getFCMRegistrationTokens(onComplete: (tokens: MutableList<String>)-> Unit){
        currentUserDocRef.get().addOnSuccessListener {
            val user = it.toObject(User::class.java)!!
            onComplete(user.registrationTokens)
        }
    }

    //sada ako hocemo da updejtamo samo jedno polje usera, odnosno tokene
    fun setFCMRegistrationTokens(registrationTokens: MutableList<String>){
        currentUserDocRef.update(mapOf("registrationTokens" to registrationTokens))
    }
    //endregion FCM
}