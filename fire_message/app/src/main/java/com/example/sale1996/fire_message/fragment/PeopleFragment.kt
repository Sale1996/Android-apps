package com.example.sale1996.fire_message.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sale1996.fire_message.AppConstants
import com.example.sale1996.fire_message.ChatActivity

import com.example.sale1996.fire_message.R
import com.example.sale1996.fire_message.recyclerview.item.PersonItem
import com.example.sale1996.fire_message.util.FirestoreUtil
import com.google.firebase.firestore.ListenerRegistration
import com.xwray.groupie.*
import kotlinx.android.synthetic.main.fragment_people.*

class PeopleFragment : Fragment() {

    // ovo cemo koristiti za firestore listener kako bi ga sacuvali
    // i onda kada se neko novi registruje mi cemo dodati u listu svih korisnika
    private lateinit var userListenerRegistration: ListenerRegistration

    private var shouldInitRecyclerView = true

    private lateinit var peopleSection: Section


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        userListenerRegistration =
            FirestoreUtil.addUsersListener(this.requireActivity(), this::updateRecyclerView)

        return inflater.inflate(R.layout.fragment_people, container, false)
    }

    private fun updateRecyclerView(items: List<Item<GroupieViewHolder>>){

        fun init(){
            recycler_view_people_fragment.apply {
                layoutManager = LinearLayoutManager(this@PeopleFragment.context)
                adapter = GroupAdapter<GroupieViewHolder>().apply {
                    peopleSection = Section(items)
                    add(peopleSection)
                    //sada dodajemo itemClicklistener kako bi otisli u chat room sa tim korisnikom
                    setOnItemClickListener(onItemClick)
                }
            }
            shouldInitRecyclerView = false
        }

        fun updateItems() = peopleSection.update(items)

        if(shouldInitRecyclerView) init()
        else updateItems()
    }

    //kada se unisti fragment, nemamo potrebe i dalje da osluskujemo promene na firebase
    override fun onDestroyView() {
        super.onDestroyView()
        FirestoreUtil.removeListener(userListenerRegistration)
        shouldInitRecyclerView = true
    }

    private val onItemClick = OnItemClickListener{ item, view ->
        if(item is PersonItem){
            val intent = Intent(activity, ChatActivity::class.java)
            intent.putExtra(AppConstants.USER_NAME, item.person.name)
            intent.putExtra(AppConstants.USER_ID, item.userId)

            startActivity(intent)
        }
    }

}
