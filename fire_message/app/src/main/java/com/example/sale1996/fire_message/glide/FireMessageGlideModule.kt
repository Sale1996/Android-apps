package com.example.sale1996.fire_message.glide

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import com.firebase.ui.storage.images.FirebaseImageLoader
import com.google.firebase.storage.StorageReference
import java.io.InputStream

/*
* Uz pomoc ovog modula mi mozemo direktno slike da pokupimo iz firebase storage-a
* */
@GlideModule
class FireMessageGlideModule: AppGlideModule() {

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        registry.append(StorageReference::class.java, InputStream::class.java,
            FirebaseImageLoader.Factory())
    }
}