package com.coin.ui.fragment

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.coin.R
import com.coin.constant.Constant
import com.coin.interpreter.OnAddListener
import com.coin.models.CurrencyList
import com.coin.models.CurrencyListResponse
import com.coin.ui.adapter.CryptoListAdapter
import com.coin.utils.PreferenceUtils
import com.coin.utils.Utils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class CryptoListFragment : BaseFragment(), OnAddListener {
    private var recyclerView: RecyclerView? = null
    private var totalCount: Int = 10000
    private var layoutManager: LinearLayoutManager? = null
    private var fetchedCurrencyDetails: List<CurrencyList> = emptyList()
    private var currencyList: MutableList<CurrencyList>? = mutableListOf()
    private var adapter: CryptoListAdapter? = null
    private val updateFetchingRunnable = Runnable {
        if (currencyList?.isEmpty() == true)
            adapter?.notifyDataSetChanged()
        else currencyList?.let { adapter?.notifyItemInserted(it.size) }
    }
    private var requestingDetails = false
    private val updateListRunnable = Runnable {
        if (!Utils.isNullOrEmpty(fetchedCurrencyDetails)) {
            val listedCount = currencyList?.size
            currencyList?.addAll(fetchedCurrencyDetails)
            adapter?.addCryptoList(fetchedCurrencyDetails)
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
            requestingDetails = false
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
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
        adapter = CryptoListAdapter(this)
        recyclerView?.adapter = adapter
        recyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                onScrollChanged()
            }
        })
        val pullToRefresh: SwipeRefreshLayout = view.findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener {
            refreshData()
            pullToRefresh.isRefreshing = false;
        }
        val currencyList = PreferenceUtils.getCryptoList(requireContext())
        if (currencyList != null)
            currencyList.let {
                fetchedCurrencyDetails = it
                Utils.runAfterRecycled(recyclerView!!, updateListRunnable)
            }
        else getItems
    }

    private fun refreshData() {
        refreshItems
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
                    currencyList?.let { PreferenceUtils.changeCryptoList(requireContext(), it) }
                    currencyList?.let { adapter?.updateFavourite(it) }
                    activity?.runOnUiThread { adapter?.notifyDataSetChanged() }
                    updateList()
                }
            }
        }
    }

    fun updateList() {
        activity?.let {
            val cryptoList: MutableList<CurrencyList> = mutableListOf()
            PreferenceUtils.getCryptoList(it)?.let { list ->
                cryptoList.addAll(list)
            }
            if (cryptoList.isNotEmpty()) {
                cryptoList.sortWith { object1: CurrencyList, object2: CurrencyList ->
                    object1.rank.compareTo(object2.rank)
                }
                currencyList?.clear()
                fetchedCurrencyDetails = emptyList()
                currencyList=cryptoList
                adapter?.setCryptoList(cryptoList)
                Utils.runAfterRecycled(recyclerView!!, updateListRunnable)
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
        }
        LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(sendIntent)
    }

    private fun handleResults(response: CurrencyListResponse) {
        hideLoader()
        fetchedCurrencyDetails = response.data
        addCryptoList(response.data)
        Utils.runAfterRecycled(recyclerView!!, updateListRunnable)
    }

    private fun handleRefreshResults(response: CurrencyListResponse) {
        hideLoader()
        currencyList?.clear()
        fetchedCurrencyDetails = emptyList()
        adapter?.removeAllCryptoList()
        fetchedCurrencyDetails = response.data
        refreshCryptoList(response.data)
        Utils.runAfterRecycled(recyclerView!!, updateListRunnable)
    }

    private fun refreshCryptoList(currencyList: ArrayList<CurrencyList>) {
        PreferenceUtils.refreshCryptoList(requireContext(), currencyList)

    }

    private fun addCryptoList(currencyList: ArrayList<CurrencyList>) {
        PreferenceUtils.addCryptoList(requireContext(), currencyList)
    }

    private fun handleError(t: Throwable) {
        hideLoader()
        Toast.makeText(
            requireContext(), "ERROR IN FETCHING API RESPONSE. Try again",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun canFetchDetails(): Boolean {
        return !requestingDetails && currencyList?.size != 0 && currencyList?.size!! < totalCount
    }

    private fun onScrollChanged() {
        try {
            if (canFetchDetails()) {
                val totalItemCount = currencyList?.size
                val lastVisibleItem = layoutManager?.findLastVisibleItemPosition() ?: 0
                if (totalItemCount != null) {
                    if (totalItemCount <= lastVisibleItem + Constant.VISIBLE_THRESHOLD) getItems
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private val refreshItems: Unit
        get() {
            requestingDetails = true
            val startCount = 1
            var limit = 0
            if (currencyList != null && currencyList?.size != 0) limit =
                currencyList?.size ?: 1
            showLoader()
            api.getAllCryptoCurrencyDetails(
                startCount,
                limit,
                Constant.SORT_ORDER
            )
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleRefreshResults, this::handleError)
            Utils.runAfterRecycled(recyclerView!!, updateFetchingRunnable)

        }
    private val getItems: Unit
        get() {
            requestingDetails = true
            var startCount = 1
            if (currencyList != null && currencyList?.size != 0) startCount =
                currencyList?.size ?: 1
            showLoader()
            api.getAllCryptoCurrencyDetails(
                startCount,
                Constant.OFFSET_VALUE,
                Constant.SORT_ORDER
            )
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleResults, this::handleError)
            Utils.runAfterRecycled(recyclerView!!, updateFetchingRunnable)

        }

    override fun addToFavorite(list: CurrencyList) {
        sendFavBroadCast(list)
    }

    override fun viewDetail(list: CurrencyList) {
        addFragmentWithBackStack(CurrencyDetailFragment.newInstance(list))
    }

    companion object {
        @JvmStatic
        fun newInstance() = CryptoListFragment()
    }
}