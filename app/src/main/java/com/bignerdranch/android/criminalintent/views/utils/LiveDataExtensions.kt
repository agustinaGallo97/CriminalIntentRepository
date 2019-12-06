package com.bignerdranch.android.criminalintent.views.utils

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

inline fun <T> LiveData<T>.observe(owner: LifecycleOwner, crossinline callable: (T) -> Unit) =
  observe(owner, Observer { callable.invoke(it) })
