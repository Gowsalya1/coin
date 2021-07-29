package com.coin.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.coin.R
import com.coin.interpreter.OnAddListener
import com.coin.models.CurrencyList


class CryptoListAdapter(val listener: OnAddListener) :
    RecyclerView.Adapter<CryptoListAdapter.CryptoViewHolder>() {
    private var currencyList: List<CurrencyList> = ArrayList()

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): CryptoViewHolder {
        val layout = LayoutInflater.from(
            viewGroup.context
        ).inflate(R.layout.recycler_item_crypto, viewGroup, false)
        return CryptoViewHolder(layout, this)
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


    override fun onBindViewHolder(holder: CryptoViewHolder, position: Int) {
        val list: CurrencyList = currencyList[position]
        holder.coinId.text = list.symbol
        holder.coinName.text = list.name
        val favIcon =
            if (list.favorite) R.drawable.ic_favorite_gold else R.drawable.ic_favorite_black
        holder.favorite.setImageResource(favIcon)
    }

    override fun getItemCount(): Int {
        return currencyList.size
    }

    fun updateFavourite(list: CurrencyList) {
        val index: Int = currencyList.indexOfFirst { it.id == list.id }
        if (index != -1)
            currencyList[index].favorite = !list.favorite
        notifyDataSetChanged()
    }



    class CryptoViewHolder(itemView: View, private val cryptoListAdapter: CryptoListAdapter) :
        RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        var coinName: TextView = itemView.findViewById(R.id.tvCoinName)
        var coinId: TextView = itemView.findViewById(R.id.tvCoinID)
        var favorite: ImageView = itemView.findViewById(R.id.ivFavorite)

        init {
            favorite.setOnClickListener(this)
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            val list: CurrencyList = cryptoListAdapter.currencyList[layoutPosition]
            if (v.id == R.id.ivFavorite) {
                cryptoListAdapter.listener.addToFavorite(list)
            } else {
                cryptoListAdapter.listener.viewDetail(list)
            }
        }

    }
}