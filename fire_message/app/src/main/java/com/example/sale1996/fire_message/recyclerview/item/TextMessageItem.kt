package com.example.sale1996.fire_message.recyclerview.item

import android.content.Context
import com.example.sale1996.fire_message.R
import com.example.sale1996.fire_message.model.TextMessage
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.item_text_message.view.*


class TextMessageItem(val message: TextMessage,
                      val context: Context)
    : MessageItem(message) {


    override fun getLayout(): Int = R.layout.item_text_message

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.textView_message_text.text = message.text
        super.bind(viewHolder, position)
    }

    //OVO redefinisemo zbog situacije jer kada posaljemo poruku, onda on updejta sve u listi
    //ali mi zelimo samo 1 item da se updejta, stoga pravimo to
    override fun isSameAs(other: Item<*>): Boolean {
        if(other !is TextMessageItem) return false
        if(this.message != other.message) return false
        return true
    }

    override fun equals(other: Any?): Boolean {
        return isSameAs((other as? TextMessageItem)!!)
    }



    override fun hashCode(): Int {
        var result = message.hashCode()
        result = 31 * result + context.hashCode()
        return result
    }

}