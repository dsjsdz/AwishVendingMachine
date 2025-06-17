package com.awish.machine.ui.components

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
import com.awish.machine.viewmodel.FloorType


@Composable
fun FloorTypeSelectorDialog(
  options: List<FloorType>,
  defaultValue: Int?,
  onSelect: (FloorType) -> Unit,
) {
  var selectedId by remember(defaultValue) {
    mutableStateOf(defaultValue)
  }

  AlertDialog(
    onDismissRequest = { },
    title = { Text(stringResource(R.string.select_floor_type)) },
    text = {
      Column {
        options.forEach { floorType ->
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 4.dp)
              .alpha(if (!floorType.disabled) 1f else 0.5f)
          ) {
            RadioButton(
              selected = selectedId == floorType.id,
              onClick = { selectedId = floorType.id },
              enabled = !floorType.disabled
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = floorType.title)
          }
        }
      }
    },
    confirmButton = {
      TextButton(
        onClick = {
          val selectedFloorType = options.find { it.id == selectedId }
          selectedFloorType?.let { onSelect(it) }
        },
        enabled = selectedId != null
      ) {
        Text(stringResource(R.string.btn_confirm))
      }
    },
    dismissButton = {}
  )
}
