package com.awish.machine.ui.components


import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun TextDivider(
  text: String,
  modifier: Modifier = Modifier,
  color: Color = DividerDefaults.color.copy(alpha = 0.3f),
  thickness: Dp = DividerDefaults.Thickness,
  textColor: Color = Color.Gray,
  textPadding: Dp = 8.dp,
  padding: Dp = 0.dp
) {
  Row(
    modifier = modifier
      .fillMaxWidth()
      .padding(padding),
    verticalAlignment = Alignment.CenterVertically
  ) {
    HorizontalDivider(
      modifier = Modifier.weight(1f),
      thickness = thickness,
      color = color
    )
    Text(
      text = text,
      color = textColor,
      modifier = Modifier.padding(horizontal = textPadding),
      style = MaterialTheme.typography.titleMedium
    )
    HorizontalDivider(
      modifier = Modifier.weight(1f),
      thickness = thickness,
      color = color
    )
  }
}
