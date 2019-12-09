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
import java.util.UUID

class CrimeFragment : Fragment(R.layout.fragment_crime) {
  companion object {
    private const val ARG_CRIME_ID = "crime_id"
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
    solvedCheckBox = view.findViewById(R.id.crimeSolved) as CheckBox

    setupDateButton()

    crimeDeatilViewModel.crimeLiveData.observe(viewLifecycleOwner) { crime ->
      crime?.let {
        this.crime = crime
        updateUI()
      }
    }
  }

  private fun setupDateButton() {
    dateButton.apply {
      text = crime.date.toString()
      isEnabled = false
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
  }

  override fun onStop() {
    super.onStop()
    crimeDeatilViewModel.saveCrime(crime)
  }

  private fun updateUI() {
    titleField.setText(crime.title)
    dateButton.text = crime.date.toString()
    solvedCheckBox.apply {
      isChecked = crime.isSolved
      jumpDrawablesToCurrentState()
    }
  }
}
