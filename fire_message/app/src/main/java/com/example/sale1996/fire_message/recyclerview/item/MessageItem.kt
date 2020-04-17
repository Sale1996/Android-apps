package com.example.sale1996.fire_message.recyclerview.item

import android.view.Gravity
import android.widget.FrameLayout
import com.example.sale1996.fire_message.R
import com.example.sale1996.fire_message.model.Message
import com.google.firebase.auth.FirebaseAuth
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.item_text_message.view.*
import org.jetbrains.anko.backgroundResource
import org.jetbrains.anko.wrapContent
import java.text.SimpleDateFormat

abstract class MessageItem(private val message: Message): Item<GroupieViewHolder>() {


    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        setTimeText(viewHolder)
        setMessageRootGravity(viewHolder)
    }

    private fun setTimeText(viewHolder: GroupieViewHolder){
        val dateFormat = SimpleDateFormat.getDateTimeInstance(SimpleDateFormat.SHORT, SimpleDateFormat.SHORT)
        viewHolder.itemView.textView_message_time.text = dateFormat.format(message.time)
    }

    /*
    * Sa ovom funkcijom mi namestamo da li ce oblacak biti sa leve ili desne strane, u zavisnosti
    * cija je poruka, nasa ili od chat partnera.
    * */
    private fun setMessageRootGravity(viewHolder: GroupieViewHolder){
        if (message.senderId == FirebaseAuth.getInstance().currentUser?.uid){
            viewHolder.itemView.message_root.apply{
                backgroundResource = R.drawable.rect_round_white
                //sada layout parametre da namestimo, ovo ce biti podeseno preko ANKO biblioteke
                val lParams = FrameLayout.LayoutParams(wrapContent, wrapContent, Gravity.END)
                this.layoutParams = lParams
            }

        }else{
            viewHolder.itemView.message_root.apply{
                backgroundResource = R.drawable.rect_round_primary_color
                //sada layout parametre da namestimo, ovo ce biti podeseno preko ANKO biblioteke
                val lParams = FrameLayout.LayoutParams(wrapContent, wrapContent, Gravity.START)
                this.layoutParams = lParams
            }
        }
    }
}