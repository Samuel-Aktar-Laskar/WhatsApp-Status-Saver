package com.cosmosrsvp.statussaver.ui.Activities

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import com.cosmosrsvp.statussaver.R
import com.cosmosrsvp.statussaver.ui.Fragments.Downloads_Fragment.Downloads_Fragment
import com.cosmosrsvp.statussaver.ui.Fragments.More_Fragment.More_Fragment
import com.cosmosrsvp.statussaver.ui.Fragments.Status_Fragment.Status_fragment
import com.cosmosrsvp.statussaver.ui.theme.StatusSaverTheme
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

