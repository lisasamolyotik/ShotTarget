package com.target.shot.model

import android.animation.ObjectAnimator
import android.content.Context
import androidx.appcompat.widget.AppCompatImageView

open class Item(val mContext: Context) : AppCompatImageView(mContext) {
    var animator: ObjectAnimator? = null
    val location = IntArray(2)

    init {
        adjustViewBounds = true
    }
}