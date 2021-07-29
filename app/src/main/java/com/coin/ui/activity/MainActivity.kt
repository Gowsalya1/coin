package com.coin.ui.activity

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.coin.R
import com.coin.ui.fragment.BaseFragment
import com.coin.ui.fragment.HomeFragment

class MainActivity : FragmentActivity() {
    private var container: FrameLayout? = null
    val requestOptions = RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        container = findViewById(R.id.container)
        showHomeScreen()
    }

    private fun showHomeScreen() {
        clearBackStack()
        replaceFragment(HomeFragment.newInstance())
    }

    private fun clearBackStack() {
        val fragmentManager = supportFragmentManager
        val count = fragmentManager.backStackEntryCount
        for (i in 0 until count) {
            fragmentManager.popBackStack()
        }
    }

    private fun replaceFragment(fragment: BaseFragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        container?.id?.let { fragmentTransaction.replace(it, fragment, fragment.javaClass.name) }
        fragmentTransaction.commit()
    }

    fun addFragmentWithBackStack(fragment: Fragment) {
        try {
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            container?.let {
                fragmentTransaction.add(it.id, fragment, fragment.javaClass.name)
                    .addToBackStack(fragment.javaClass.name).commit()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    fun backPressed(view: View?) {
        onBackPressed()
    }
}