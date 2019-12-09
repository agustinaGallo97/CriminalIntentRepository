package com.bignerdranch.android.criminalintent.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bignerdranch.android.criminalintent.R
import com.bignerdranch.android.criminalintent.controllers.CrimeFragment
import com.bignerdranch.android.criminalintent.controllers.CrimeListFragment
import com.bignerdranch.android.criminalintent.views.utils.Router
import java.util.UUID

class MainActivity : AppCompatActivity(), Router {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    val currentFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer)
    if (currentFragment == null) {
      val fragment = CrimeListFragment.newInstance()
      supportFragmentManager
        .beginTransaction()
        .add(R.id.fragmentContainer, fragment).commit()
    }
  }

  override fun openCrimeDetailsView(crimeId: UUID) {
    val fragment = CrimeFragment.newInstance(crimeId)
    supportFragmentManager
      .beginTransaction()
      .replace(R.id.fragmentContainer, fragment)
      .addToBackStack(null)
      .commit()
  }
}
