package com.coin.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.coin.R
import com.coin.constant.Constant
import com.coin.models.CurrencyDetails
import com.coin.models.CurrencyDetailsResponse
import com.coin.models.CurrencyList
import com.coin.utils.DateUtils
import com.coin.utils.ImageUtils
import com.coin.utils.PreferenceUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class CurrencyDetailFragment : BaseFragment() {
    private var currencyList: CurrencyList? = null
    private var ivLogo: ImageView? = null
    private var descriptionContent: TextView? = null
    private var category: TextView? = null
    private var dateAdded: TextView? = null
    private var favorite: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            currencyList = it.getParcelable(Constant.CURRENCY_DETAILS)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_currency_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val headerTitle: TextView = view.findViewById(R.id.tvHeaderTitle)
        val coinName: TextView = view.findViewById(R.id.tvCoinName)
        val coinId: TextView = view.findViewById(R.id.tvCoinID)
        favorite = view.findViewById(R.id.ivFavorite)
        val tvFHData: TextView = view.findViewById(R.id.tvFHData)
        val tvLHData: TextView = view.findViewById(R.id.tvLHData)
        val platformName: TextView = view.findViewById(R.id.tvPlatformName)
        val platformSymbol: TextView = view.findViewById(R.id.tvPlatformSymbol)
        descriptionContent = view.findViewById(R.id.tvDescriptionContent)
        category = view.findViewById(R.id.tvCategoryData)
        dateAdded = view.findViewById(R.id.tvDADate)
        ivLogo = view.findViewById(R.id.ivLogo)
        favorite?.setOnClickListener(this)

        val currencyDetails =
            currencyList?.id?.let { PreferenceUtils.getCryptoDetails(it, requireContext()) }
        if (currencyDetails != null) {
            setDetails(currencyDetails)
        } else
            getDetails
        headerTitle.text = currencyList?.name
        coinName.text = currencyList?.name
        coinId.text = currencyList?.symbol
        tvLHData.text = DateUtils.getFormattedDate(currencyList?.last_historical_data!!)
        tvFHData.text = DateUtils.getFormattedDate(currencyList?.first_historical_data!!)
        if (currencyList?.platform != null) {
            platformName.text = currencyList?.platform?.name
            platformSymbol.text = currencyList?.platform?.symbol
        } else {
            view.findViewById<TextView>(R.id.tvPlatform).visibility = View.GONE
            platformName.visibility = View.GONE
            platformSymbol.visibility = View.GONE
        }
        refreshFav()
    }

    private fun refreshFav() {
        val favIcon = if (currencyList?.favorite == true) R.drawable.ic_favorite_gold
        else R.drawable.ic_favorite_black
        favorite?.setImageResource(favIcon)
    }

    override fun onClick(v: View) {
        if (v.id == R.id.ivFavorite) {
            sendFavBroadCast()
        }
    }


    private fun sendFavBroadCast() {
        val sendIntent = Intent(Constant.FAVORITE_UPDATED).apply {
            putExtra(Constant.CURRENCY_DETAILS, currencyList)
            putExtra(Constant.FAVORITE, currencyList?.favorite)
        }
        LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(sendIntent)
        currencyList?.favorite = !(currencyList?.favorite ?: false)
        refreshFav()
    }


    private fun handleResults(response: CurrencyDetailsResponse) {
        val data = (((response.data as Map<*, *>)[currencyList?.id]) as Map<*, *>)
        val currencyDetails = CurrencyDetails(data)
        currencyList?.id?.let {
            PreferenceUtils.setCryptoDetails(
                it,
                requireContext(),
                currencyDetails = currencyDetails
            )
        }
        setDetails(currencyDetails)
        hideLoader()
    }

    private fun setDetails(currencyDetails: CurrencyDetails) {
        descriptionContent?.text = currencyDetails.description
        category?.text = currencyDetails.category
        dateAdded?.text = DateUtils.getFormattedDate(currencyDetails.date_added)
        ImageUtils.loadImage(requireContext(), currencyDetails.logo, ivLogo)
    }


    private fun handleError(t: Throwable) {
        hideLoader()
        Toast.makeText(
            requireContext(), "ERROR IN FETCHING API RESPONSE. Try again",
            Toast.LENGTH_LONG
        ).show()
    }

    private val getDetails: Unit
        get() {
            showLoader()
            api.getAllCryptoCurrencyDetails(currencyList?.id ?: "")
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleResults, this::handleError)
        }

    companion object {
        @JvmStatic
        fun newInstance(currencyList: CurrencyList) =
            CurrencyDetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(Constant.CURRENCY_DETAILS, currencyList)
                }
            }
    }
}