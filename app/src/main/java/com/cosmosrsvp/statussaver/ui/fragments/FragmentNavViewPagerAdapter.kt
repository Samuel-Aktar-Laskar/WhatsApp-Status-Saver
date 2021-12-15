package com.cosmosrsvp.statussaver.ui.fragments

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.cosmosrsvp.statussaver.ui.fragments.downloads_fragment.Downloads_Fragment
import com.cosmosrsvp.statussaver.ui.fragments.more_fragment.More_Fragment
import com.cosmosrsvp.statussaver.ui.fragments.status_fragment.Status_fragment

private const val NUM_TABS=3

class FragmentNavViewPagerAdapter
    constructor(
        fragmentManager: FragmentManager,
        lifecycle: Lifecycle
    )
    : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return NUM_TABS
    }

    override fun createFragment(position: Int): Fragment {
        when(position) {
            0-> return Status_fragment()
            1-> return Downloads_Fragment()
        }
        return More_Fragment()
    }
}