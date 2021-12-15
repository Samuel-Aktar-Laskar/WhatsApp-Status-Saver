package com.cosmosrsvp.statussaver.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cosmosrsvp.statussaver.R
import com.cosmosrsvp.statussaver.ui.fragments.downloads_fragment.Downloads_Fragment
import com.cosmosrsvp.statussaver.ui.fragments.more_fragment.More_Fragment
import com.cosmosrsvp.statussaver.ui.fragments.status_fragment.Status_fragment
import com.google.android.material.tabs.TabLayout


class MainActivity : AppCompatActivity() {
    val TAG ="MainActivityTag"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       setContentView(R.layout.main_activity)
        val tabLayout : TabLayout
        tabLayout=findViewById(R.id.tabLayout)
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                // Handle tab select
                if (tab != null) {
                    when (tab.position){
                        0 -> //status
                            supportFragmentManager.
                            beginTransaction().
                            replace(R.id.nav_host_fragment , Status_fragment()).
                            commit()
                        1 -> //downloads
                            supportFragmentManager.
                            beginTransaction().
                            replace(R.id.nav_host_fragment , Downloads_Fragment()).
                            commit()
                        2 ->  //more
                            supportFragmentManager.
                            beginTransaction().
                            replace(R.id.nav_host_fragment , More_Fragment()).
                            commit()
                        else -> print("Nothing")
                    }
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // Handle tab reselect
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // Handle tab unselect
            }
        })
    }
}

