package com.cosmosrsvp.statussaver.ui.Activities.ShowImage

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.cosmosrsvp.statussaver.domain.util.isVideo
import java.io.File

class ViewPagerAdapter
constructor(
    fa: AppCompatActivity,
    val fileList: ArrayList<File>
    ): FragmentStateAdapter(fa) {

    override fun getItemCount(): Int {
        return fileList.size
    }

    override fun createFragment(position: Int): Fragment {
        val file=fileList.get(position)
        if(file.isVideo()){
            return PlayVideoFragment.newInstance(fileList.get(position).absolutePath)
        }
        else
        return ViewImageFragment.newInstance(fileList.get(position))
    }

}