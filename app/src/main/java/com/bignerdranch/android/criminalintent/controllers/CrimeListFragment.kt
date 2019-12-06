package com.bignerdranch.android.criminalintent.controllers

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.criminalintent.CrimeAdapter
import com.bignerdranch.android.criminalintent.R
import com.bignerdranch.android.criminalintent.models.Crime
import com.bignerdranch.android.criminalintent.models.CrimeListViewModel

private const val TAG = "CrimeListFragment"

class CrimeListFragment : Fragment(R.layout.fragment_crime_list) {
  companion object {
    fun newInstance(): CrimeListFragment = CrimeListFragment()
  }

  private lateinit var crimeRecyclerView: RecyclerView
  private var adapter: CrimeAdapter? = CrimeAdapter(emptyList())

  private val crimeListViewModel: CrimeListViewModel by lazy {
    ViewModelProviders.of(this).get(CrimeListViewModel::class.java)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    crimeListViewModel.crimeListLiveData.observe(
      viewLifecycleOwner,
      Observer { crimes ->
        crimesObserver(crimes)
      }
    )
    setUpCrimeRecyclerView(view)
  }

  private fun setUpCrimeRecyclerView(view: View) {
    crimeRecyclerView = view.findViewById(R.id.crimeRecyclerView) as RecyclerView
    crimeRecyclerView.layoutManager = LinearLayoutManager(context)
    crimeRecyclerView.adapter = adapter
  }

  private fun crimesObserver(crimes: List<Crime>) {
    crimes?.let {
      Log.i(TAG, "Got crimes ${crimes.size}")
      updateUI(crimes)
    }
  }

  private fun updateUI(crimes: List<Crime>) {
    adapter = CrimeAdapter(crimes)
    crimeRecyclerView.adapter = adapter
  }
}
