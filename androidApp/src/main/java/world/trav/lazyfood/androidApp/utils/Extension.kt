package world.trav.lazyfood.androidApp.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View

//
// Created by HJ Chin on 14/9/20.
//
fun View.fadeOut(){
    this.animate().alpha(0f).setDuration(1000).setListener(object: AnimatorListenerAdapter(){
        override fun onAnimationEnd(animation: Animator) {
            this@fadeOut.visibility = View.GONE
        }
    })
}

fun View.fadeIn(){
    this.alpha = 0f
    this.visibility = View.VISIBLE
    this.animate().alpha(1f).duration = 2000
}
