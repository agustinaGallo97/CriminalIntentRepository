package com.bignerdranch.android.criminalintent.controllers

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.criminalintent.CrimeAdapter
import com.bignerdranch.android.criminalintent.R
import com.bignerdranch.android.criminalintent.models.CrimeListViewModel

private const val TAG = "CrimeListFragment"

class CrimeListFragment : Fragment(R.layout.fragment_crime_list) {
  companion object {
    fun newInstance(): CrimeListFragment = CrimeListFragment()
  }

  private lateinit var crimeRecyclerView: RecyclerView
  private var adapter: CrimeAdapter? = null

  private val crimeListViewModel: CrimeListViewModel by lazy {
    ViewModelProviders.of(this).get(CrimeListViewModel::class.java)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    Log.d(TAG, "Total crimes: ${crimeListViewModel.crimes.size}")
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    crimeRecyclerView = view.findViewById(R.id.crimeRecyclerView) as RecyclerView
    crimeRecyclerView.layoutManager = LinearLayoutManager(context)

    updateUI()
  }

  private fun updateUI() {
    val crimes = crimeListViewModel.crimes
    adapter = CrimeAdapter(crimes)
    crimeRecyclerView.adapter = adapter
  }
}
