package com.bignerdranch.android.criminalintent

import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.criminalintent.models.Crime

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
      dateTextView.text = DateFormat.format("dd/MM/yyyy", crime.date)

      with(itemView) {
        var text = context.getString(R.string.crime_title_pressed, crime.title)
        setOnClickListener {
          Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
        }
      }
      solvedImageView.visibility = if (crime.isSolved) View.VISIBLE else View.GONE
    }
  }
}
