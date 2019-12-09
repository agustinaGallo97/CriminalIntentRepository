package com.bignerdranch.android.criminalintent

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.bignerdranch.android.criminalintent.database.CrimeDatabase
import com.bignerdranch.android.criminalintent.models.Crime
import java.util.UUID
import java.util.concurrent.Executors

class CrimeRepository private constructor() {
  companion object {
    private const val DATABASE_NAME = "crime-database"
    private var INSTANCE: CrimeRepository? = null

    fun initialize(context: Context) {
      if (INSTANCE == null) INSTANCE = CrimeRepository()
    }

    fun get(): CrimeRepository {
      return INSTANCE ?: throw IllegalStateException("CrimeRepository must be initialized")
    }
  }

  private val database: CrimeDatabase =
    Room.databaseBuilder(CriminalIntentApplication.context, CrimeDatabase::class.java, DATABASE_NAME).build()

  private val crimeDao = database.crimeDao()
  private val executor = Executors.newSingleThreadExecutor()

  fun getCrimes(): LiveData<List<Crime>> = crimeDao.getCrimes()

  fun getCrime(id: UUID): LiveData<Crime?> = crimeDao.getCrime(id)

  fun updateCrime(crime: Crime) =
    executor.execute {
      crimeDao.updateCrime(crime)
    }

  fun addCrime(crime: Crime) =
    executor.execute {
      crimeDao.addCrime(crime)
    }
}
