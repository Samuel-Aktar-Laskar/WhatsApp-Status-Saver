package com.cosmosrsvp.statussaver.ui.Activities.ShowImage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.cosmosrsvp.statussaver.R
import com.cosmosrsvp.statussaver.domain.util.isVideo
import java.io.File

class ViewImageActivity: AppCompatActivity() {
    val LIST_NAME="dLoadMedFiLst"
    val CURRENT_POSITION="currentPosi"
    private lateinit var viewPager: ViewPager2
    private val TAG = "viewImgaeActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_view_image)
        if (intent.hasExtra(LIST_NAME) || intent.hasExtra(CURRENT_POSITION)){
            viewPager=findViewById(R.id.pager)
            val FilePathList=intent.getStringArrayListExtra(LIST_NAME)
            if (FilePathList==null){
                super.onBackPressed()
            }
            val FileList: ArrayList<File> = FilePathList?.map{
                File(it)
            } as ArrayList<File>

            val pagerAdapter=ViewPagerAdapter(this, FileList.map {
                if(it.isVideo()){
                    PlayVideoFragment.newInstance(it.absolutePath)
                } else
                    ViewImageFragment.newInstance(it)
            } as ArrayList<Fragment>)
            viewPager.adapter=pagerAdapter
            val passedPosition=intent.getIntExtra(CURRENT_POSITION,0)
            viewPager.currentItem=passedPosition
            Log.d(TAG, "Position passed: ${passedPosition}")
            viewPager?.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                        Log.d(TAG, "Page position: $position")
                }

                override fun onPageScrollStateChanged(state: Int) {
                    super.onPageScrollStateChanged(state)
                    Log.d(TAG, "Page Scrolled: $state")

                }

                override fun onPageScrolled(position: Int,
                                            positionOffset: Float,
                                            positionOffsetPixels: Int) {
                    super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                    Log.d(TAG, "On page scrolled: $positionOffset and page position: $position")
                    if (passedPosition!=position){
                        pagerAdapter.fragmentList[position].onPause()
                    }
                }
            })
        }
    }
}