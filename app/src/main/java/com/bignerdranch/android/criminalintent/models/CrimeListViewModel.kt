package com.bignerdranch.android.criminalintent.models

import androidx.lifecycle.ViewModel

class CrimeListViewModel : ViewModel() {
  val LIST_LENGHT = 100

  val crimes: MutableList<Crime> = mutableListOf()

  init {
    mockData()
  }

  private fun mockData() {
    for (i in 0 until LIST_LENGHT) {
      val crime = Crime()
      crime.title = "Crime #$i"
      crime.isSolved = i % 2 == 0
      crimes += crime
    }
  }
}
