package com.bignerdranch.android.criminalintent.controllers

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import java.util.Calendar
import java.util.Date

class TimePickerFragment : DialogFragment() {
  companion object {
    private const val ARG_TIME = "time"

    fun newInstance(time: Date): TimePickerFragment {
      val args = Bundle().apply {
        putSerializable(ARG_TIME, time)
      }

      return TimePickerFragment().apply {
        arguments = args
      }
    }
  }

  interface Callbacks {
    fun onTimeSelected(time: Date)
  }

  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    val calendar = Calendar.getInstance()

    val timeListener = TimePickerDialog.OnTimeSetListener { _: TimePicker, hour: Int, minute: Int ->
      calendar.set(Calendar.HOUR_OF_DAY, hour)
      calendar.set(Calendar.MINUTE, minute)
      val resultTime: Date = calendar.time

      targetFragment?.let { fragment -> (fragment as Callbacks).onTimeSelected(resultTime) }
    }
    val time = arguments?.getSerializable(ARG_TIME) as Date
    calendar.time = time
    val initialHour = calendar.get(Calendar.HOUR_OF_DAY)
    val initialMinute = calendar.get(Calendar.MINUTE)

    return TimePickerDialog(requireContext(), timeListener, initialHour, initialMinute, true)
  }
}
