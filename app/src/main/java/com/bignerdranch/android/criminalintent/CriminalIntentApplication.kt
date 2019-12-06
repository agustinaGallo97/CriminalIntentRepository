package com.bignerdranch.android.criminalintent

import android.app.Application

class CriminalIntentApplication : Application() {
  companion object {
    lateinit var context: CriminalIntentApplication
  }

  override fun onCreate() {
    super.onCreate()
    context = this
    CrimeRepository.initialize(this)
  }
}
