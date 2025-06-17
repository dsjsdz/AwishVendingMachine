package com.awish.machine

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay

@Composable
fun Splash(navController: NavHostController) {
  val timeLeft = remember { mutableIntStateOf(2) }

  // 启动倒计时（仅用作显示用，不自动跳转）
  LaunchedEffect(Unit) {
    while (timeLeft.intValue > 0) {
      delay(3000)
      timeLeft.intValue -= 1
      if (timeLeft.intValue == 0) {
        navController.navigate("main") {
          popUpTo("splash") { inclusive = true }
        }
      }
    }
  }

  Box(modifier = Modifier.fillMaxSize()) {
    // 全屏广告图
    Image(
      painter = painterResource(id = R.drawable.splash),
      contentDescription = stringResource(id = R.string.nav_splash),
      contentScale = ContentScale.Crop,
      modifier = Modifier.fillMaxSize()
    )

    // 跳过按钮（点击后才跳转）
    if (timeLeft.intValue > 0) {
      Box(
        modifier = Modifier
          .align(Alignment.TopEnd)
          .padding(16.dp)
      ) {
        Button(
          enabled = true,
          onClick = {
            navController.navigate("main") {
              popUpTo("splash") { inclusive = true }
            }
          },
          shape = RoundedCornerShape(20.dp),
          colors = ButtonDefaults.buttonColors(
            containerColor = Color.Black.copy(alpha = 0.3f),
            disabledContainerColor = Color.Black.copy(alpha = 0.3f),
            disabledContentColor = Color.White.copy(alpha = 0.5f)
          ),
          contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
        ) {
          Text(
            text = stringResource(id = R.string.countdown, timeLeft.intValue),
            color = Color.White
          )
        }
      }
    }

    // 底部中间的“进入主页”按钮
    Box(
      modifier = Modifier
        .align(Alignment.BottomCenter)
        .padding(bottom = 32.dp)
    ) {
      Button(
        onClick = {
          navController.navigate("main") {
            popUpTo("splash") { inclusive = true }
          }
        }
      ) {
        Text(text = stringResource(id = R.string.to_homepage))
      }
    }
  }
}
