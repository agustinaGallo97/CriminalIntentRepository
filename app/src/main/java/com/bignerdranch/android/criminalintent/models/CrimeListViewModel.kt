package com.bignerdranch.android.criminalintent.models

import androidx.lifecycle.ViewModel
import com.bignerdranch.android.criminalintent.CrimeRepository

class CrimeListViewModel : ViewModel() {
  private val crimeRepository = CrimeRepository.get()
  val crimeListLiveData = crimeRepository.getCrimes()

  fun addCrime(crime: Crime) = crimeRepository.addCrime(crime)
}
