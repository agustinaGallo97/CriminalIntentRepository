package com.bignerdranch.android.criminalintent

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.criminalintent.models.Crime
import com.bignerdranch.android.criminalintent.views.utils.context

class CrimeAdapter(var crimes: List<Crime>) : RecyclerView.Adapter<CrimeAdapter.CrimeHolder>() {
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrimeHolder {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_crime, parent, false)
    return CrimeHolder(view)
  }

  override fun onBindViewHolder(holder: CrimeHolder, position: Int) {
    val crime = crimes[position]
    holder.bind(crime)
  }

  override fun getItemCount(): Int = crimes.size

  inner class CrimeHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val titleTextView: TextView = itemView.findViewById(R.id.crimeTitle)
    private val dateTextView: TextView = itemView.findViewById(R.id.crimeDate)
    private val solvedImageView: ImageView = itemView.findViewById(R.id.crimeSolved)

    fun bind(crime: Crime) {
      titleTextView.text = crime.title
      dateTextView.text =
        DateUtils.formatDateTime(context, crime.date.time, DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_YEAR)
      itemView.setOnClickListener {
        val text = context.getString(R.string.crime_title_pressed, crime.title)
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
      }
      solvedImageView.isVisible = crime.isSolved
    }
  }
}
