package com.cosmosrsvp.statussaver.ui.Activities.ShowImage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.cosmosrsvp.statussaver.R
import java.io.File

class ViewImageActivity: AppCompatActivity() {
    val LIST_NAME="dLoadMedFiLst"
    val CURRENT_POSITION="currentPosi"
    private lateinit var viewPager: ViewPager2
    companion object pageNo {
        var PageNo: Int=0
    }
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
            val pagerAdapter=ViewPagerAdapter(this,FileList)
            viewPager.adapter=pagerAdapter
            viewPager.currentItem=intent.getIntExtra(CURRENT_POSITION,0)
            viewPager?.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                        PageNo=position
                }

                override fun onPageScrollStateChanged(state: Int) {
                    super.onPageScrollStateChanged(state)
                }

                override fun onPageScrolled(position: Int,
                                            positionOffset: Float,
                                            positionOffsetPixels: Int) {
                    super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                }
            })

        }


    }

}