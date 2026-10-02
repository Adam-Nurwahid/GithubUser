package com.damtoy.githubuser.presentation.util

import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.damtoy.githubuser.R

fun ImageView.loadAvatar(url: String) {
    Glide.with(this)
        .load(url)
        .placeholder(R.drawable.ic_person)
        .error(R.drawable.ic_person)
        .circleCrop()
        .into(this)
}

/** Shows the text, or hides the view when the value is null/blank. */
fun TextView.setTextOrGone(value: String?) {
    isVisible = !value.isNullOrBlank()
    text = value.orEmpty()
}