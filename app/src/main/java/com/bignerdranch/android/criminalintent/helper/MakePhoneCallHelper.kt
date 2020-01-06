package com.bignerdranch.android.criminalintent.helper

import android.content.Intent
import android.net.Uri
import androidx.fragment.app.Fragment

object MakePhoneCallHelper {
  fun makePhoneCall(phoneNumber: String, fragment: Fragment) {
    val dialIntent = Intent(Intent.ACTION_DIAL)
    dialIntent.data = Uri.parse("tel:$phoneNumber")
    fragment.startActivity(dialIntent)
  }
}
