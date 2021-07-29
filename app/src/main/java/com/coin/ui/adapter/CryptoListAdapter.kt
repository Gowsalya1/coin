package com.coin.ui.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.coin.R
import com.coin.interpreter.OnAddListener
import com.coin.models.CurrencyList


class CryptoListAdapter(val listener: OnAddListener) : BaseRecyclerAdapter() {
    private var currencyList: List<CurrencyList> = ArrayList()

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): BaseViewHolder {
        return CryptoViewHolder(getItemView(viewGroup, viewType), this)
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.recycler_item_crypto
    }

    fun setCryptoList(list: List<CurrencyList>) {
        currencyList = list
    }

    fun addCryptoList(list: List<CurrencyList>) {
        currencyList = currencyList + list
    }

    fun removeAllCryptoList() {
        currencyList = emptyList()
    }

    override fun getItemCount(): Int {
        return currencyList.size
    }

    fun updateFavourite(list: CurrencyList) {
        currencyList.map {
            if (it.id == list.id)
                it.favorite = !list.favorite
        }
        notifyDataSetChanged()
    }


    class CryptoViewHolder(itemView: View, private val cryptoListAdapter: CryptoListAdapter) :
        BaseViewHolder(itemView) {
        private var coinName: TextView = itemView.findViewById(R.id.tvCoinName)
        private var coinId: TextView = itemView.findViewById(R.id.tvCoinID)
        private var favorite: ImageView = itemView.findViewById(R.id.ivFavorite)

        init {
            favorite.setOnClickListener(this)
            itemView.setOnClickListener(this)
        }

        override fun bindData(position: Int) {
            super.bindData(position)
            val list: CurrencyList = cryptoListAdapter.currencyList[position]
            coinId.text = list.symbol
            coinName.text = list.name
            val favIcon =
                if (list.favorite) R.drawable.ic_favorite_gold else R.drawable.ic_favorite_black
            favorite.setImageResource(favIcon)
        }

        override fun onClick(v: View) {
            super.onClick(v)
            val list: CurrencyList = cryptoListAdapter.currencyList[layoutPosition]
            if (v.id == R.id.ivFavorite) {
                cryptoListAdapter.listener.addToFavorite(list)
            } else {
                cryptoListAdapter.listener.viewDetail(list)
            }
        }

    }
}