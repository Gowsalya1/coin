package com.coin.ui.fragment

import android.content.BroadcastReceiver
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.coin.R
import com.coin.api.ApiService
import com.coin.ui.activity.MainActivity
import com.coin.utils.Utils
import java.util.*

open class BaseFragment : Fragment(), View.OnClickListener {
    val api: ApiService
        get() = ApiService.newInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentManager?.addOnBackStackChangedListener {
            val current = fragmentManager?.findFragmentById(R.id.container)
            if (current?.isHidden == true) {
                fragmentManager?.beginTransaction()?.show(current)?.commit()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currentFragment = javaClass.name
    }

    override fun onDestroy() {
        super.onDestroy()
        currentFragment = ""
    }
    override fun onDestroyView() {
        super.onDestroyView()
        unregisterReceiver()
        removeAll()
    }

    private fun removeReceiver(receiver: BroadcastReceiver) {
        try {
            requireContext().unregisterReceiver(receiver)
        } catch (e: Exception) {
//            if (BuildConfig.DEBUG) e.printStackTrace()
        }
    }

    fun showLoader() {
        Utils.showLoader(requireActivity())
    }

    fun hideLoader() {
        Utils.hideLoader()
    }

    open fun unregisterReceiver() {
        for (receiver in broadcastReceivers) {
            for ((key, value) in receiver) {
                if (currentFragment?.javaClass?.simpleName == key) {
                    removeReceiver(value)
                    Log.d("registerReceiver remove", receiver.javaClass.name)
                    broadcastReceivers.remove(receiver)
                    return
                }
            }
        }
    }

    fun removeAll() {
        for (receiver in broadcastReceivers) {
            for ((_, value) in receiver) {
                removeReceiver(value)
            }
        }
        broadcastReceivers.clear()
    }

    open val mainActivity: MainActivity?
        get() {
            return if (activity is MainActivity) activity as MainActivity? else null
        }

    fun addReceiver(receiver: BroadcastReceiver): Boolean {
        val className = currentFragment?.javaClass?.simpleName
        val map: MutableMap<String, BroadcastReceiver> = HashMap()
        map[className ?: ""] = receiver
        return if (!broadcastReceivers.contains(map)) {
            broadcastReceivers.add(map)
            true
        } else false
    }

    fun onBackPressed() {
        activity?.onBackPressed()
    }

    override fun onClick(v: View) {}

    open fun addFragmentWithBackStack(fragment: Fragment?) {
        mainActivity?.addFragmentWithBackStack(fragment!!)
    }

    companion object {
        var currentFragment: String? = null
            private set

        private val broadcastReceivers = LinkedList<Map<String, BroadcastReceiver>>()
    }

}