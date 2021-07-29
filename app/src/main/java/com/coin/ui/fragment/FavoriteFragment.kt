package com.coin.ui.fragment

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.coin.R
import com.coin.constant.Constant
import com.coin.interpreter.OnAddListener
import com.coin.models.CurrencyList
import com.coin.ui.adapter.FavListAdapter
import com.coin.utils.PreferenceUtils
import com.coin.utils.Utils

class FavoriteFragment : BaseFragment(), OnAddListener {

    private var recyclerView: RecyclerView? = null
    private var layoutManager: LinearLayoutManager? = null
    private var fetchedCurrencyDetails: List<CurrencyList> = emptyList()
    private var currencyList: MutableList<CurrencyList>? = mutableListOf()
    private var adapter: FavListAdapter? = null
    private val updateListRunnable = Runnable {
        if (!Utils.isNullOrEmpty(fetchedCurrencyDetails)) {
            val listedCount = currencyList?.size
            currencyList?.addAll(fetchedCurrencyDetails)
            adapter?.setCryptoList(fetchedCurrencyDetails)
            if (listedCount == 0) {
                adapter?.notifyDataSetChanged()
            } else {
                listedCount?.let {
                    fetchedCurrencyDetails.size.let { it1 ->
                        adapter?.notifyItemRangeInserted(
                            it,
                            it1
                        )
                    }
                    adapter?.notifyItemRemoved(it)
                    adapter?.notifyDataSetChanged()
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list_crypto, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerReceiver()
        recyclerView = view.findViewById(R.id.rvListCrypto)
        layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recyclerView?.layoutManager = layoutManager
        adapter = FavListAdapter(this)
        recyclerView?.adapter = adapter
        updateList()
    }

    fun updateList() {
        activity?.let {
            val favList: MutableList<CurrencyList> = mutableListOf()
            PreferenceUtils.getCryptoList(it)?.forEach { list ->
                if (list.favorite)
                    favList.add(list)
            }
            if (favList.isNotEmpty()) {
                favList.sortWith { object1: CurrencyList, object2: CurrencyList ->
                    object1.rank.compareTo(object2.rank)
                }
                fetchedCurrencyDetails = favList
                Utils.runAfterRecycled(recyclerView!!, updateListRunnable)
            } else adapter?.removeAllCryptoList()

        }
    }

    override fun onDestroyView() {
        try {
            super.onDestroyView()
            unregisterBroadCast()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun registerReceiver() {
        val filter = IntentFilter()
        filter.addAction(Constant.FAVORITE_UPDATED)
        LocalBroadcastManager.getInstance(requireContext())
            .registerReceiver(broadCastListener, filter)
        addReceiver(broadCastListener)
    }

    private val broadCastListener = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                Constant.FAVORITE_UPDATED -> {
                    val currencyList: CurrencyList? =
                        intent.getParcelableExtra(Constant.CURRENCY_DETAILS)
                    val favorite: Boolean =
                        intent.getBooleanExtra(Constant.FAVORITE, false)
                    currencyList?.favorite = favorite
                    currencyList?.let {
                        PreferenceUtils.changeCryptoList(
                            requireContext(),
                            it
                        )
                    }
                    currencyList?.let { adapter?.updateFavourite(it) }
                    updateList()
                }
            }
        }
    }

    private fun unregisterBroadCast() {
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(broadCastListener)
        unregisterReceiver()
    }

    private fun sendFavBroadCast(list: CurrencyList) {
        val sendIntent = Intent(Constant.FAVORITE_UPDATED).apply {
            putExtra(Constant.CURRENCY_DETAILS, list)
            putExtra(Constant.FAVORITE, list.favorite)
            currencyList
        }
        LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(sendIntent)
    }

    override fun addToFavorite(list: CurrencyList) {
        sendFavBroadCast(list)
    }

    override fun viewDetail(list: CurrencyList) {
        addFragmentWithBackStack(CurrencyDetailFragment.newInstance(list))
    }

    companion object {
        @JvmStatic
        fun newInstance() = FavoriteFragment()
    }
}