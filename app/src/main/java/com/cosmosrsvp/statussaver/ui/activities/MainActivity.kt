package com.cosmosrsvp.statussaver.ui.activities


import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.cosmosrsvp.statussaver.R
import com.cosmosrsvp.statussaver.ui.fragments.FragmentNavViewPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import android.content.Intent
import android.net.Uri


import androidx.activity.result.contract.ActivityResultContracts

import android.content.Context
import android.os.Build

import android.os.storage.StorageManager
import androidx.annotation.RequiresApi
import com.cosmosrsvp.statussaver.util.TREE_URI
import com.cosmosrsvp.statussaver.util.URI_PATH


val tabTitles= arrayOf(
    "STATUS",
    "DOWNLOADS",
    "MORE"
)

class MainActivity : AppCompatActivity() {
   // val TAG ="MainActivityTag"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       setContentView(R.layout.main_activity)
        val tabLayout : TabLayout = findViewById(R.id.tabLayout)
        val viewPager2: ViewPager2=findViewById(R.id.fragment_navigation_viewpager)
        viewPager2.reduceDragSensitivity(3)

        val navAdapter = FragmentNavViewPagerAdapter(
            supportFragmentManager,
            lifecycle)

        viewPager2.adapter=navAdapter

        TabLayoutMediator(tabLayout,viewPager2){tab, position->
            tab.text= tabTitles[position]
        }.attach()


    }
    private fun ViewPager2.reduceDragSensitivity(f: Int = 4) {
        val recyclerViewField = ViewPager2::class.java.getDeclaredField("mRecyclerView")
        recyclerViewField.isAccessible = true
        val recyclerView = recyclerViewField.get(this) as RecyclerView

        val touchSlopField = RecyclerView::class.java.getDeclaredField("mTouchSlop")
        touchSlopField.isAccessible = true
        val touchSlop = touchSlopField.get(recyclerView) as Int
        touchSlopField.set(recyclerView, touchSlop*f)       // "8" was obtained experimentally
    }





}



