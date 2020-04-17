package com.example.sale1996.fire_message.recyclerview.item

import android.content.Context
import com.example.sale1996.fire_message.R
import com.example.sale1996.fire_message.glide.GlideApp
import com.example.sale1996.fire_message.model.ImageMessage
import com.example.sale1996.fire_message.util.StorageUtil
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.item_image_message.view.*


class ImageMessageItem(val message: ImageMessage,
                       val context: Context): MessageItem(message) {

    override fun getLayout(): Int = R.layout.item_image_message

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        super.bind(viewHolder, position) //pozivamo nad klasu da resi zajednicke stvari

        GlideApp.with(context)
            .load(StorageUtil.pathToReference(message.imagePath))
            .placeholder(R.drawable.ic_image_black_24dp)
            .into(viewHolder.itemView.imageView_message_image)
    }

    //OVO redefinisemo zbog situacije jer kada posaljemo poruku, onda on updejta sve u listi
    //ali mi zelimo samo 1 item da se updejta, stoga pravimo to
    override fun isSameAs(other: Item<*>): Boolean {
        if(other !is ImageMessageItem) return false
        if(this.message != other.message) return false
        return true
    }

    override fun equals(other: Any?): Boolean {
        return isSameAs((other as? ImageMessageItem)!!)
    }



    override fun hashCode(): Int {
        var result = message.hashCode()
        result = 31 * result + context.hashCode()
        return result
    }
}