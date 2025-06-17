package com.awish.machine.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.awish.machine.R

@Composable
fun LoadingDialog(isLoading: Boolean) {
  if (isLoading) {
    Dialog(onDismissRequest = {}) {
      Box(
        modifier = Modifier
          .size(100.dp)
          .background(Color.White, shape = RoundedCornerShape(12.dp)),
        contentAlignment = Alignment.Center
      ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          CircularProgressIndicator()
          Spacer(Modifier.height(8.dp))
          Text(stringResource(R.string.loading), color = Color.Gray)
        }
      }
    }
  }
}
