package com.awish.machine.ui.templates

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class LogLevel {
  INFO, WARN, ERROR, DEBUG, SUCCESS, DANGER
}

fun LogLevel.getColor(): Color = when (this) {
  LogLevel.INFO -> Color.White
  LogLevel.WARN -> Color(0xFFFFC107)   // amber
  LogLevel.ERROR -> Color(0xFFFF5252)  // red
  LogLevel.DEBUG -> Color(0xFF80CBC4)  // teal
  LogLevel.SUCCESS -> Color(0xFF4CAF50) // green
  LogLevel.DANGER -> Color(0xFFE57373)  // deep orange
}

data class LogEntry(val timestamp: Long, val message: String, val level: LogLevel = LogLevel.INFO) {
  companion object {
    fun new(message: String, info: LogLevel = LogLevel.INFO) =
      LogEntry(System.currentTimeMillis(), message, info)
  }
}

@Composable
fun Logger(logs: List<LogEntry>, onClear: () -> Unit) {
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .fillMaxHeight(0.5f)
      .background(Color(0xFF1E1E1E))
  ) {
    LazyColumn(
      modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
        .align(Alignment.TopCenter),
      verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
      items(logs) { log ->
        LogItem(log)
      }
    }
  }
}


fun formatTimestamp(timestamp: Long): String {
  val formatter = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
  return formatter.format(Date(timestamp))
}

@Composable
fun LogItem(log: LogEntry) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .background(Color(0xFF2A2A2A), shape = RoundedCornerShape(4.dp))
      .padding(horizontal = 8.dp, vertical = 6.dp)
  ) {
    Text(
      text = "[${formatTimestamp(log.timestamp)}]",
      color = Color(0xFF9E9E9E),
      fontSize = 13.sp,
      fontFamily = FontFamily.Monospace,
      modifier = Modifier.padding(end = 8.dp)
    )
    Text(
      text = log.message,
      color = log.level.getColor(),
      fontSize = 13.sp,
      fontFamily = FontFamily.Monospace
    )
  }
}

