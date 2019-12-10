package com.bignerdranch.android.criminalintent.controllers

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
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
import kotlinx.android.synthetic.main.fragment_crime_list.*
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
      onCrimeSelectedListener = { crime ->
        val text = context?.getString(R.string.crime_title_pressed, crime.title)
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
        router.openCrimeDetailsView(crime.id)
      }
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
        if (crimes.size == 0) setEmptyListView() else setListView(crimes)
      }
    }
  }

  private fun setListView(crimes: List<Crime>) {
    emptyCrimeListAdvert.setVisibility(View.GONE)
    addCrimeButton.setVisibility(View.GONE)
    updateUI(crimes)
  }

  private fun setEmptyListView() {
    emptyCrimeListAdvert.setVisibility(View.VISIBLE)
    addCrimeButton.setVisibility(View.VISIBLE)
    addCrimeButton.setOnClickListener { v ->
      addNewCrime()
    }
  }

  private fun setUpCrimeRecyclerView(view: View) {
    crimeRecyclerView = view.findViewById(R.id.crimeRecyclerView)
    crimeRecyclerView.layoutManager = LinearLayoutManager(context)
    crimeRecyclerView.adapter = adapter
  }

  private fun updateUI(crimes: List<Crime>) = adapter.submitList(crimes)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setHasOptionsMenu(true)
  }

  override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
    super.onCreateOptionsMenu(menu, inflater)
    inflater.inflate(R.menu.fragment_crime_list, menu)
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return when (item.itemId) {
      R.id.newCrime -> {
        addNewCrime()
        true
      }
      else -> return super.onOptionsItemSelected(item)
    }
  }

  private fun addNewCrime() {
    val crime = Crime()
    crimeListViewModel.addCrime(crime)
    router.openCrimeDetailsView(crime.id)
  }
}
