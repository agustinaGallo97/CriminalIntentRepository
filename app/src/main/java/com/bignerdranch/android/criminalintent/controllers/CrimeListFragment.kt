package com.bignerdranch.android.criminalintent.controllers

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.criminalintent.CrimeAdapter
import com.bignerdranch.android.criminalintent.R
import com.bignerdranch.android.criminalintent.models.Crime
import com.bignerdranch.android.criminalintent.models.CrimeListViewModel
import com.bignerdranch.android.criminalintent.views.utils.Router
import com.bignerdranch.android.criminalintent.views.utils.observe
import timber.log.Timber

class CrimeListFragment : Fragment(R.layout.fragment_crime_list) {
  companion object {
    fun newInstance(): CrimeListFragment = CrimeListFragment()
  }

  private val router: Router
    get() = activity as Router

  private lateinit var crimeRecyclerView: RecyclerView
  private val adapter: CrimeAdapter = CrimeAdapter()
    .apply {
      onCrimeSelectedListener = { crimeId -> router.openCrimeDetailsView(crimeId) }
    }
  private val crimeListViewModel: CrimeListViewModel by lazy {
    ViewModelProviders.of(this).get(CrimeListViewModel::class.java)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    setupCrimeData()
    setUpCrimeRecyclerView(view)
  }

  private fun setupCrimeData() {
    crimeListViewModel.crimeListLiveData.observe(
      viewLifecycleOwner
    ) { crimes ->
      crimes.let {
        Timber.d("Got crimes ${crimes.size}")
        updateUI(crimes)
      }
    }
  }

  private fun setUpCrimeRecyclerView(view: View) {
    crimeRecyclerView = view.findViewById<RecyclerView>(R.id.crimeRecyclerView)
    crimeRecyclerView.layoutManager = LinearLayoutManager(context)
    crimeRecyclerView.adapter = adapter
  }

  private fun updateUI(crimes: List<Crime>) = adapter.submitList(crimes)
}
