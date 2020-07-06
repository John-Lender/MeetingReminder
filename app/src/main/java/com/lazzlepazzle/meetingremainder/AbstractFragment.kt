package com.lazzlepazzle.meetingremainder

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

abstract class AbstractFragment: AppCompatActivity() {
    abstract fun createFragment(): Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.i("ABSTRACT_FRAGMENT", "create Fragment")
        val fragmentManager: FragmentManager = supportFragmentManager
        var fragment: Fragment? = fragmentManager.findFragmentById(R.id.fragment_container)

        if (fragment == null){
            fragment = createFragment()
            fragmentManager.beginTransaction().add(R.id.fragment_container, fragment).commit()
        }
    }
}