package com.awish.machine.viewmodel

import android.serialport.SerialPortFinder
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SerialPortViewModel @Inject constructor() : ViewModel() {
  // 获取串口地址列表（可后续替换为通过接口注入 Finder）
  val serialPaths: List<String> = SerialPortFinder().allDevicesPath.sorted()

  // 当前选中的串口路径
  var selectedPath by mutableStateOf<String?>(null)
    private set

  fun selectPath(path: String) {
    selectedPath = path
  }
}
