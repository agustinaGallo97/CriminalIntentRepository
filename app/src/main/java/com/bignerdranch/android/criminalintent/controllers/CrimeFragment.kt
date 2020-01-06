package com.bignerdranch.android.criminalintent.controllers

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.bignerdranch.android.criminalintent.views.utils.observe
import androidx.lifecycle.ViewModelProviders
import com.bignerdranch.android.criminalintent.models.Crime
import com.bignerdranch.android.criminalintent.models.CrimeDetailViewModel
import com.bignerdranch.android.criminalintent.views.utils.BaseTextWatcher
import android.text.format.DateFormat
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RelativeLayout
import java.text.SimpleDateFormat
import java.util.Date
import java.util.UUID
import android.widget.Toast
import androidx.core.content.FileProvider
import com.bignerdranch.android.criminalintent.R
import com.bignerdranch.android.criminalintent.getScaledBitmap
import com.bignerdranch.android.criminalintent.helper.MakePhoneCallHelper
import com.bignerdranch.android.criminalintent.helper.PhoneContactHelper
import java.io.File

class CrimeFragment : Fragment(R.layout.fragment_crime), DatePickerFragment.Callbacks, TimePickerFragment.Callbacks {
  companion object {
    private const val TYPE_INTENT = "text/plain"
    private const val ARG_CRIME_ID = "crime_id"
    private const val DIALOG_DATE = "DialogDate"
    private const val REQUEST_DATE = 0
    private const val REQUEST_CONTACT = 1
    private const val REQUEST_PHOTO = 2
    private const val DIALOG_TIME = "DialogTime"
    private const val REQUEST_TIME = 0
    private const val DATE_FORMAT = "yyyy-MM-dd"
    private const val TIME_FORMAT = "HH:mm"
    private const val DATE_FORMAT_REPORT = "EEE, MM, dd"
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
  private lateinit var photoFile: File
  private lateinit var photoUri: Uri
  private lateinit var titleField: EditText
  private lateinit var dateButton: Button
  private lateinit var timeButton: Button
  private lateinit var solvedCheckBox: CheckBox
  private lateinit var reportButton: Button
  private lateinit var suspectButton: Button
  private lateinit var photoButton: ImageButton
  private lateinit var photoView: ImageView

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
    setUpVars(view)

    crimeDeatilViewModel.crimeLiveData.observe(viewLifecycleOwner) { crime ->
      crime?.let {
        this.crime = crime
        photoFile = crimeDeatilViewModel.getPhotoFile(crime)
        photoUri = FileProvider.getUriForFile(
          requireActivity(), "com.bignerdranch.android.criminalintent.fileprovider", photoFile
        )
        updateUI()
      }
    }
  }

  override fun onStart() {
    super.onStart()

    val titleWatcher = object : BaseTextWatcher {
      override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        crime = crime.copy(title = s.toString())
      }
    }

    titleField.addTextChangedListener(titleWatcher)

    solvedCheckBox.apply {
      setOnCheckedChangeListener { _, isChecked -> crime = crime.copy(isSolved = isChecked) }
    }

    buttonsListener()
  }

  private fun buttonsListener() {
    pickerButtons()

    reportButton.setOnClickListener {
      Intent(Intent.ACTION_SEND).apply {
        type = TYPE_INTENT
        putExtra(Intent.EXTRA_TEXT, getCrimeReport())
        putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_subject))
      }.also { intent ->
        val chooserIntent = Intent.createChooser(intent, getString(R.string.send_report))
        startActivity(chooserIntent)
      }
    }

    suspectButton.apply {
      val pickContactIntent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
      setOnClickListener { startActivityForResult(pickContactIntent, REQUEST_CONTACT) }

      val packageManager: PackageManager = requireActivity().packageManager
      val resolvedActivity: ResolveInfo? =
        packageManager.resolveActivity(pickContactIntent, PackageManager.MATCH_DEFAULT_ONLY)
      if (resolvedActivity == null) isEnabled = false
    }

    photoButton()
  }

  override fun onStop() {
    super.onStop()
    crimeDeatilViewModel.saveCrime(crime)
  }

  override fun onDetach() {
    super.onDetach()
    requireActivity().revokeUriPermission(photoUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
  }

  override fun onDateSelected(date: Date) {
    crime = crime.copy(date = date)
    updateUI()
  }

  override fun onTimeSelected(dateTime: Date) {
    crime.date.time = dateTime.time
    updateUI()
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    when {
      resultCode != Activity.RESULT_OK -> return
      resultCode == REQUEST_CONTACT && data != null -> onContactInformationReceived(data.data!!)
      resultCode == REQUEST_PHOTO -> {
        requireActivity().revokeUriPermission(photoUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        updatePhotoView()
      }
    }
  }

  private fun onContactInformationReceived(contactUri: Uri) {
    val (suspectName, phoneNumber) = PhoneContactHelper.getContactNameAndPhoneNumber(contactUri)

    crime = crime.copy(suspectName = suspectName)
    crimeDeatilViewModel.saveCrime(crime)
    suspectButton.text = suspectName
    Toast.makeText(context, "$suspectName: $phoneNumber", Toast.LENGTH_SHORT).show()
    MakePhoneCallHelper.makePhoneCall(phoneNumber!!, this)
  }

  private fun setUpVars(view: View) {
    titleField = view.findViewById(R.id.crimeTitle) as EditText
    dateButton = view.findViewById(R.id.crimeDate) as Button
    timeButton = view.findViewById(R.id.crimeTime) as Button
    solvedCheckBox = view.findViewById(R.id.crimeSolved) as CheckBox
    reportButton = view.findViewById(R.id.crimeReport) as Button
    suspectButton = view.findViewById(R.id.crimeSuspect) as Button
    photoButton = view.findViewById(R.id.crimeCamera) as ImageButton
    photoView = view.findViewById(R.id.crimePhoto) as ImageView
  }

  private fun updateUI() {
    titleField.setText(crime.title)
    dateButton.text = dateFormatter.format(crime.date)
    timeButton.text = timeFormatter.format(crime.date)
    solvedCheckBox.apply {
      isChecked = crime.isSolved
      jumpDrawablesToCurrentState()
    }
    if (crime.suspectName.isNotEmpty()) {
      suspectButton.text = crime.suspectName
    }
    updatePhotoView()
  }

  private fun updatePhotoView() {
    if (photoFile.exists()) {
      val bitmap = getScaledBitmap(photoFile.path, requireActivity())
      photoView.setImageBitmap(bitmap)
    } else {
      photoView.setImageDrawable(null)
    }
  }

  private fun getCrimeReport(): String {
    val solvedString =
      if (crime.isSolved) getString(R.string.crime_report_solved) else getString(R.string.crime_report_unsolved)
    val dateString = DateFormat.format(DATE_FORMAT_REPORT, crime.date).toString()
    var suspect = if (crime.suspectName.isBlank()) getString(R.string.crime_report_no_suspect) else getString(
      R.string.crime_report_suspect, crime.suspectName
    )
    return getString(R.string.crime_report, crime.title, dateString, solvedString, suspect)
  }

  private fun photoButton() {
    photoButton.apply {
      val packageManager: PackageManager = requireActivity().packageManager
      val captureImage = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
      val resolvedActivity: ResolveInfo? =
        packageManager.resolveActivity(captureImage, PackageManager.MATCH_DEFAULT_ONLY)
      if (resolvedActivity == null) isEnabled = false

      setOnClickListener {
        captureImage.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
        val cameraActivities: List<ResolveInfo> =
          packageManager.queryIntentActivities(captureImage, PackageManager.MATCH_DEFAULT_ONLY)
        for (cameraActivity in cameraActivities) {
          requireActivity().grantUriPermission(
            cameraActivity.activityInfo.packageName,
            photoUri,
            Intent.FLAG_GRANT_WRITE_URI_PERMISSION
          )
        }
        startActivityForResult(captureImage, REQUEST_PHOTO)
      }
    }

    photoView.setOnClickListener {
      val builder = Dialog(context!!)
      builder.requestWindowFeature(Window.FEATURE_NO_TITLE)
      builder.window!!.setBackgroundDrawable(
        ColorDrawable(Color.TRANSPARENT)
      )
      builder.setOnDismissListener {}

      val imageView = ImageView(context)
      imageView.setImageURI(photoUri)
      builder.addContentView(
        imageView, RelativeLayout.LayoutParams(
          ViewGroup.LayoutParams.MATCH_PARENT,
          ViewGroup.LayoutParams.MATCH_PARENT
        )
      )
      builder.show()
    }
  }

  private fun pickerButtons() {
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
}
