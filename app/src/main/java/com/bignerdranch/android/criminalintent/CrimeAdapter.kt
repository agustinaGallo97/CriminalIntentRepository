package com.bignerdranch.android.criminalintent

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

  inner class CrimeHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
    private lateinit var crime: Crime

    private val titleTextView: TextView = itemView.findViewById(R.id.crimeTitle)
    private val dateTextView: TextView = itemView.findViewById(R.id.crimeDate)

    init {
      itemView.setOnClickListener(this)
    }

    fun bind(crime: Crime) {
      this.crime = crime
      titleTextView.text = this.crime.title
      dateTextView.text = this.crime.date.toString()
    }

    override fun onClick(v: View) {
      Toast.makeText(itemView.context, "${crime.title} pressed!", Toast.LENGTH_SHORT).show()
    }
  }
}
