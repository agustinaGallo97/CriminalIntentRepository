package com.bignerdranch.android.criminalintent.controllers

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.criminalintent.CrimeAdapter
import com.bignerdranch.android.criminalintent.R
import com.bignerdranch.android.criminalintent.models.CrimeListViewModel

private const val TAG = "CrimeListFragment"

class CrimeListFragment : Fragment() {
  companion object {
    fun newInstance(): CrimeListFragment {
      return CrimeListFragment()
    }
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

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    val view = inflater.inflate(R.layout.fragment_crime_list, container, false)
    crimeRecyclerView = view.findViewById(R.id.crimeRecyclerView) as RecyclerView
    crimeRecyclerView.layoutManager = LinearLayoutManager(context)

    updateUI()

    return crimeRecyclerView
  }

  private fun updateUI() {
    val crimes = crimeListViewModel.crimes
    adapter = CrimeAdapter(crimes)
    crimeRecyclerView.adapter = adapter
  }

}
