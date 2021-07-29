package com.coin.utils

import android.content.Context
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.coin.R
import com.coin.ui.activity.MainActivity

object ImageUtils {

    fun loadImage(context: Context?, imageURL: String?, imageView: ImageView?): Boolean {
        if (!Utils.isNullOrWhiteSpace(imageURL)) {
            Glide.with(context!!)
                .load(imageURL!!)
                .apply(Utils.getRequestOptions(MainActivity()))
                .thumbnail(.50f)
                .error(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_launcher_background
                    )
                )
                .into(imageView!!)
        } else return true
        return false
    }
}

