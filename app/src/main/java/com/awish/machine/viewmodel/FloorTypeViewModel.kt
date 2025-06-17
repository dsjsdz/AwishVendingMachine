package com.awish.machine.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.awish.machine.R
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

data class FloorType(val id: Int, val title: String, val disabled: Boolean = false)

@HiltViewModel
class FloorTypeViewModel @Inject constructor(application: Application) : ViewModel() {
  @SuppressLint("StaticFieldLeak")
  private val context = application.applicationContext

  val options: List<FloorType> = listOf(
    FloorType(1, context.getString(R.string.spring_motor)),
    FloorType(2, context.getString(R.string.electromagnetic)),
    FloorType(3, context.getString(R.string.caterpillar)),
  )

  var selectedFloorType by mutableStateOf<Int?>(null)
    private set

  fun select(id: Int) {
    selectedFloorType = id
  }
}
