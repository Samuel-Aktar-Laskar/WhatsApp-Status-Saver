package com.cosmosrsvp.statussaver.ui.Activities.ShowImage

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter


class ViewPagerAdapter
constructor(
    fa: AppCompatActivity,
    val fragmentList: ArrayList<Fragment>
    ): FragmentStateAdapter(fa) {

    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        val fragment=fragmentList.get(position)
        return  fragment

    }

}