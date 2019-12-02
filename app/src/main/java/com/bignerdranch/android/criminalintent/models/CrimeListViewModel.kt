package com.bignerdranch.android.criminalintent.models

import androidx.lifecycle.ViewModel

class CrimeListViewModel : ViewModel() {
  companion object {
    const val LIST_LENGHT = 100
  }

  val crimes: MutableList<Crime> = mutableListOf()

  init {

    for (i in 0 until Companion.LIST_LENGHT) {
      val crime = Crime()
      crime.title = "Crime #$i"
      crime.isSolved = i % 2 == 0
      crimes += crime
    }
  }
}