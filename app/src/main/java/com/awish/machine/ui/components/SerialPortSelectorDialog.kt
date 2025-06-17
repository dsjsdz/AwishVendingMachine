package com.awish.machine.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.awish.machine.R


@Composable
fun SerialPortSelectorDialog(
  serialPaths: List<String>,
  defaultValue: String?,
  onSelect: (String) -> Unit,
) {
  var selectedPath by remember(defaultValue) {
    mutableStateOf(defaultValue)
  }

  AlertDialog(
    onDismissRequest = { },
    title = { Text(stringResource(id = R.string.select_serial_path)) },
    text = {
      Column {
        serialPaths.forEach { path ->
          val isEnabled = path.startsWith("/dev/ttyS")
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
              .fillMaxWidth()
              .let {
                if (isEnabled) it.clickable { selectedPath = path } else it
              }
              .padding(vertical = 4.dp)
              .alpha(if (isEnabled) 1f else 0.5f)
          ) {
            RadioButton(
              selected = selectedPath == path,
              onClick = { selectedPath = path },
              enabled = isEnabled
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = path)
          }
        }
      }
    },
    confirmButton = {
      TextButton(
        onClick = {
          selectedPath?.let {
            onSelect(it)
          }
        },
        enabled = selectedPath != null
      ) {
        Text(text = stringResource(id = R.string.btn_confirm))
      }
    },
    dismissButton = {}
  )
}
