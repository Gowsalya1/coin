package com.coin.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager2.widget.ViewPager2
import com.coin.R
import com.coin.ui.adapter.HomePagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment : BaseFragment() {
    private var currentSelectedItem: Int = 0
    private var homePagerAdapter: HomePagerAdapter? = null
    private var viewPager: ViewPager2? = null
    private var tabLayout: TabLayout? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val headerTitle: TextView = view.findViewById(R.id.tvHeaderTitle)
        headerTitle.text = getString(R.string.cmc)
        view.findViewById<ImageView>(R.id.ivHeaderBack).visibility = View.GONE
        viewPager = view.findViewById(R.id.viewPager)
        tabLayout = view.findViewById(R.id.tabLayout)
        homePagerAdapter = HomePagerAdapter(activity)
        viewPager?.adapter = homePagerAdapter
        viewPager?.registerOnPageChangeCallback(pageChangeListener)
        TabLayoutMediator(tabLayout!!, viewPager!!) { tab, position ->
            tab.text = if (position == 0)
                getString(R.string.cryptoCurrecy)
            else getString(R.string.favorite)
        }.attach()
    }

    private val pageChangeListener: ViewPager2.OnPageChangeCallback =
        object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                currentSelectedItem = position
                if (position == 1)
                    homePagerAdapter?.favoriteFragment?.updateList()
                    super.onPageSelected(position)
            }
        }

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }
}