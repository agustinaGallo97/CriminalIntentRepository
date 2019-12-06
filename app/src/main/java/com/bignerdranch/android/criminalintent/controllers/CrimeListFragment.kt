package com.bignerdranch.android.criminalintent.controllers

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.criminalintent.CrimeAdapter
import com.bignerdranch.android.criminalintent.R
import com.bignerdranch.android.criminalintent.models.Crime
import com.bignerdranch.android.criminalintent.models.CrimeListViewModel
import timber.log.Timber

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

  inline fun <T> LiveData<T>.observe(owner: LifecycleOwner, crossinline callable: (T) -> Unit) =
    observe(owner, Observer { callable.invoke(it) })

  private fun setUpCrimeRecyclerView(view: View) {
    crimeRecyclerView = view.findViewById(R.id.crimeRecyclerView) as RecyclerView
    crimeRecyclerView.layoutManager = LinearLayoutManager(context)
    crimeRecyclerView.adapter = adapter
  }

  private fun updateUI(crimes: List<Crime>) {
    adapter = CrimeAdapter(crimes)
    crimeRecyclerView.adapter = adapter
  }
}
