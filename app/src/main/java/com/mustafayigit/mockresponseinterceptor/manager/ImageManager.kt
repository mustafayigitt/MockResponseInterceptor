package com.mustafayigit.mockresponseinterceptor.manager

import android.widget.ImageView
import com.bumptech.glide.Glide

object ImageManager {

    fun loadImage(target: ImageView, source: String) {
        Glide.with(target.context)
            .load(source)
            .into(target)
    }

}
