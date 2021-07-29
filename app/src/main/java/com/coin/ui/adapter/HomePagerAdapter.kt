package com.coin.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.coin.ui.fragment.BaseFragment
import com.coin.ui.fragment.CryptoListFragment
import com.coin.ui.fragment.FavoriteFragment


class HomePagerAdapter(activity: FragmentActivity?) : FragmentStateAdapter(activity!!) {
     val favoriteFragment = FavoriteFragment.newInstance()
     val cryptoListFragment = CryptoListFragment.newInstance()
    val fragmentList: List<BaseFragment> = listOf(cryptoListFragment, favoriteFragment)


    private fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }


    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return getItem(position)
    }
}