package com.bignerdranch.android.criminalintent.helper

import android.net.Uri
import android.provider.ContactsContract
import com.bignerdranch.android.criminalintent.CriminalIntentApplication

object PhoneContactHelper {
  fun getContactNameAndPhoneNumber(contactUri: Uri): Pair<String, String?> {
    var nameAndPhoneNumber: Pair<String, String>? = "" to ""
    val queryFields = arrayOf(
      ContactsContract.CommonDataKinds.Phone.NUMBER,
      ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
    )
    val cursor = CriminalIntentApplication.context.contentResolver.query(
      contactUri, queryFields, null, null, null
    )
    cursor?.use {
      it.moveToFirst()
      val numberColumn = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
      val nameColumn = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
      nameAndPhoneNumber = it.getString(numberColumn) to it.getString(nameColumn)
    }
    return nameAndPhoneNumber!!
  }
}
