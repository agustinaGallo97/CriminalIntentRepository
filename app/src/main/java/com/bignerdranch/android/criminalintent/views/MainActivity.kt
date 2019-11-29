package com.bignerdranch.android.criminalintent.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bignerdranch.android.criminalintent.R
import com.bignerdranch.android.criminalintent.controllers.CrimeFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer)
        if (currentFragment == null) {
            val fragment = CrimeFragment()
            supportFragmentManager.beginTransaction().add(R.id.fragmentContainer, fragment).commit()
        }
    }
}
