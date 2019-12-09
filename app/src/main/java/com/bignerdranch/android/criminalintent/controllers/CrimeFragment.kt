package com.bignerdranch.android.criminalintent.controllers

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.bignerdranch.android.criminalintent.views.utils.observe
import androidx.lifecycle.ViewModelProviders
import com.bignerdranch.android.criminalintent.R
import com.bignerdranch.android.criminalintent.models.Crime
import com.bignerdranch.android.criminalintent.models.CrimeDetailViewModel
import com.bignerdranch.android.criminalintent.views.utils.BaseTextWatcher
import java.text.SimpleDateFormat
import java.util.Date
import java.util.UUID

class CrimeFragment : Fragment(R.layout.fragment_crime), DatePickerFragment.Callbacks, TimePickerFragment.Callbacks {
  companion object {
    private const val ARG_CRIME_ID = "crime_id"
    private const val DIALOG_DATE = "DialogDate"
    private const val REQUEST_DATE = 0
    private const val DIALOG_TIME = "DialogTime"
    private const val REQUEST_TIME = 0
    private const val DATE_FORMAT = "yyyy-MM-dd"
    private const val TIME_FORMAT = "HH:mm"
    private val dateFormatter = SimpleDateFormat(DATE_FORMAT)
    private val timeFormatter = SimpleDateFormat(TIME_FORMAT)

    fun newInstance(crimeId: UUID): CrimeFragment {
      val args = Bundle().apply {
        putSerializable(ARG_CRIME_ID, crimeId)
      }
      return CrimeFragment().apply { arguments = args }
    }
  }

  private lateinit var crime: Crime
  private lateinit var titleField: EditText
  private lateinit var dateButton: Button
  private lateinit var timeButton: Button
  private lateinit var solvedCheckBox: CheckBox

  private val crimeDeatilViewModel: CrimeDetailViewModel by lazy {
    ViewModelProviders.of(this).get(CrimeDetailViewModel::class.java)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    crime = Crime()
    val crimeId: UUID = arguments?.getSerializable(ARG_CRIME_ID) as UUID
    crimeDeatilViewModel.loadCrime(crimeId)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    titleField = view.findViewById(R.id.crimeTitle) as EditText
    dateButton = view.findViewById(R.id.crimeDate) as Button
    timeButton = view.findViewById(R.id.crimeTime)
    solvedCheckBox = view.findViewById(R.id.crimeSolved) as CheckBox

    crimeDeatilViewModel.crimeLiveData.observe(viewLifecycleOwner) { crime ->
      crime?.let {
        this.crime = crime
        updateUI()
      }
    }
  }

  override fun onStart() {
    super.onStart()
    val titleWatcher = object : BaseTextWatcher {
      override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        crime.title = s.toString()
      }
    }

    titleField.addTextChangedListener(titleWatcher)

    solvedCheckBox.apply {
      setOnCheckedChangeListener { _, isChecked -> crime.isSolved = isChecked }
    }

    dateButton.setOnClickListener {
      DatePickerFragment.newInstance(crime.date).apply {
        setTargetFragment(this@CrimeFragment, REQUEST_DATE)
        show(this@CrimeFragment.requireFragmentManager(), DIALOG_DATE)
      }
    }

    timeButton.setOnClickListener {
      TimePickerFragment.newInstance(crime.date).apply {
        setTargetFragment(this@CrimeFragment, REQUEST_TIME)
        show(this@CrimeFragment.requireFragmentManager(), DIALOG_TIME)
      }
    }
  }

  override fun onStop() {
    super.onStop()
    crimeDeatilViewModel.saveCrime(crime)
  }

  override fun onDateSelected(date: Date) {
    crime.date = date
    updateUI()
  }

  override fun onTimeSelected(dateTime: Date) {
    crime.date.time = dateTime.time
    updateUI()
  }

  private fun updateUI() {
    titleField.setText(crime.title)
    dateButton.text = dateFormatter.format(crime.date)
    timeButton.text = timeFormatter.format(crime.date)
    solvedCheckBox.apply {
      isChecked = crime.isSolved
      jumpDrawablesToCurrentState()
    }
  }
}
