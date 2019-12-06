package com.bignerdranch.android.criminalintent.database

import androidx.room.TypeConverter
import java.util.Date
import java.util.UUID

class CrimeTypeConverters {
  @TypeConverter
  fun fromDate(date: Date?): Long? {
    return date?.time
  }

  @TypeConverter
  fun toDate(millisSinceEpoch: Long?): Date? = millisSinceEpoch?.let {
    Date(it)
  }


  @TypeConverter
  fun fromUUID(uuid: UUID?): String? = uuid?.toString()

  @TypeConverter
  fun toUUID(uuid: String?): UUID? = UUID.fromString(uuid)
}
