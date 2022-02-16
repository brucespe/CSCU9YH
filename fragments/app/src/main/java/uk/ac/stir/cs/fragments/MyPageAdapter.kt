package uk.ac.stir.cs.fragments

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
class MyPageAdapter(fa: FragmentActivity, private val mNumOfTabs: Int) :
    FragmentStateAdapter(fa) {
    /**
     * Getting the number of tabs
     */
    override fun getItemCount(): Int {
        return mNumOfTabs
    }
    /**
     * Creating fragments with positions
     */
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> Page1()
            1 -> page2()
            2 -> page3()
            else -> Page1()
        }
    }
}