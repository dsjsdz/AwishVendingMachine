package com.awish.machine

import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun About(navController: NavHostController) {
  val version = Build.VERSION.SDK_INT
  val versionName = Build.VERSION.RELEASE
  val appVersion = BuildConfig.VERSION_NAME

  val tableData = listOf(
    stringResource(id = R.string.model) to Build.MODEL,
    stringResource(id = R.string.manufacturer) to Build.MANUFACTURER,
    stringResource(id = R.string.platform) to Build.BRAND,
    stringResource(id = R.string.release_version) to versionName,
    stringResource(id = R.string.sdk_version) to version.toString(),
    stringResource(id = R.string.version_name) to appVersion,
    stringResource(id = R.string.technical_support) to stringResource(id = R.string.contacts),
  )

  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text(text = stringResource(id = R.string.nav_about)) },
        navigationIcon = {
          IconButton(onClick = {
            navController.popBackStack()
          }) {
            Icon(
              Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = stringResource(id = R.string.nav_back),
            )
          }
        },
      )
    }
  ) { innerPadding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
        .padding(24.dp),
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Image(
        painter = painterResource(id = R.drawable.android),
        contentDescription = "logo",
        modifier = Modifier
          .width(250.dp)
          .height(250.dp),
        contentScale = ContentScale.Crop
      )

      Text(
        text = stringResource(id = R.string.app_name),
        modifier = Modifier.padding(bottom = 8.dp)
      )

      Spacer(modifier = Modifier.height(64.dp))

      tableData.forEach { (label, value) ->
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .background(Color(0xFFF5F5F5), shape = RoundedCornerShape(4.dp))
            .padding(12.dp),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Text(text = label, style = MaterialTheme.typography.bodyMedium)
          Text(text = value, style = MaterialTheme.typography.bodyMedium)
        }
      }

      Spacer(modifier = Modifier.weight(1f))

      // 底部版权信息
      val currentYear = Calendar.getInstance().get(Calendar.YEAR)
      Text(
        text = "Copyright © 2014-$currentYear AWISH, Version: $appVersion",
        style = MaterialTheme.typography.bodySmall,
        modifier = Modifier.align(Alignment.CenterHorizontally)
      )
    }
  }
}
