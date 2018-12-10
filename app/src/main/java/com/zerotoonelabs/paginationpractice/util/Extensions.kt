package com.zerotoonelabs.paginationpractice.util

import android.widget.ImageView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.zerotoonelabs.paginationpractice.GlideRequests

fun ImageView.loadWithGlide(url: String, glide: GlideRequests, highDensity: Boolean = false){
    val circularProgressDrawable = CircularProgressDrawable(context)
    circularProgressDrawable.strokeWidth = 10f
    circularProgressDrawable.centerRadius = 30f
    circularProgressDrawable.start()
    glide.load((if(highDensity) UrlProvider.IMAGE_BASE_URL_HIGH_RESOLUTION else UrlProvider.IMAGE_BASE_URL_LOW_RESOLUTION) + url)
        .centerInside()
        .placeholder(circularProgressDrawable)
        .into(this)
}