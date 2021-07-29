package com.coin.utils

import android.app.Activity
import android.app.AlertDialog
import android.os.Build
import android.os.Handler
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.RequestOptions
import com.coin.R
import com.coin.constant.Constant
import com.coin.ui.activity.MainActivity

object Utils {
    private var dialogLoader: AlertDialog? = null

    @JvmStatic
    fun isNullOrEmpty(list: List<*>?): Boolean {
        return list == null || list.isEmpty()
    }

    @JvmStatic
    fun isNullOrWhiteSpace(s: String?): Boolean {
        return s == null || s.trim { it <= ' ' } == "" || s.trim { it <= ' ' }.equals("null", ignoreCase = true)
    }

    fun getRequestOptions(activity: MainActivity): RequestOptions {
        return activity.requestOptions
    }
    @JvmStatic
    fun runAfterRecycled(recyclerView: RecyclerView, runnable: Runnable) {
        if (recyclerView.isComputingLayout || recyclerView.isAnimating) {
            recyclerView.postDelayed({ runAfterRecycled(recyclerView, runnable) }, 100)
        } else recyclerView.post(runnable)
    }

    @JvmStatic
    fun hideLoader() {
        if (dialogLoader != null && dialogLoader!!.isShowing) {
            dialogLoader!!.dismiss()
            dialogLoader = null
        }
    }

    @JvmOverloads
    @JvmStatic
    fun showLoader(activity: FragmentActivity, timeout: Long = Constant.API_TIMEOUT.toLong()) {
        try {
            hideLoader()
            val builder = AlertDialog.Builder(activity)
            val view = LayoutInflater.from(activity).inflate(R.layout.progress_dialog, null, false)
            builder.setView(view)
            dialogLoader = builder.create()
            dialogLoader?.setCancelable(false)
            dialogLoader?.show()
            val window = dialogLoader?.window
            window?.let {
                it.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                it.setGravity(Gravity.CENTER)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    it.setBackgroundDrawable(ContextCompat.getDrawable(activity, R.color.transparent_shadow))
                } else it.setBackgroundDrawable(ContextCompat.getDrawable(activity, R.color.transparent_shadow))
            }
            Handler(activity.mainLooper).postDelayed({ -> hideLoader() }, timeout)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}