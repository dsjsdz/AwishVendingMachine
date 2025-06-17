package com.awish.machine.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.awish.machine.R


@Composable
fun ChannelSelectorDialog(
  defaultValue: Int?,
  onSelect: (Int) -> Unit,
) {
  var code by remember(defaultValue) {
    mutableStateOf(defaultValue)
  }
  val rows = (0..99).chunked(10)

  AlertDialog(
    onDismissRequest = { },
    title = { Text(stringResource(R.string.select_channel)) },
    text = {
      Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        rows.forEach { rowItems ->
          Row(
            modifier = Modifier
              .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            rowItems.forEach { id ->
              Card(
                modifier = Modifier
                  .weight(1f)
                  .aspectRatio(1f)
                  .clickable { code = id },
                colors = CardDefaults.cardColors(
                  containerColor = if (code == id) Color(0xFF448AFF) else Color(0xFFEEEEEE)
                )
              ) {
                Box(
                  modifier = Modifier.fillMaxSize(),
                  contentAlignment = Alignment.Center
                ) {
                  Text(text = "$id")
                }
              }
            }
          }
        }
      }
    },
    confirmButton = {
      TextButton(
        onClick = {
          code?.let {
            onSelect(it)
          }
        },
        enabled = code != null
      ) {
        Text(stringResource(R.string.btn_confirm))
      }
    },
    dismissButton = {}
  )
}
