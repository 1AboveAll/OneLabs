package com.himanshurawat.onelabs.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.himanshurawat.onelabs.ui.fragment.ViewContactFragment
import com.himanshurawat.onelabs.ui.fragment.contact.ContactFragment

class FragmentAdapter(fragmentManager: FragmentManager): FragmentPagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment? {

        when(position){
            0 -> return ContactFragment()
            1 -> return ViewContactFragment()
        }

        return ContactFragment()
    }

    override fun getCount(): Int {

        return 2
    }

}