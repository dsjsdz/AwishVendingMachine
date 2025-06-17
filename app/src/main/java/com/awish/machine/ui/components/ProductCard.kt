package com.awish.machine.ui.components

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import cc.uling.usdk.board.UBoard
import cc.uling.usdk.board.wz.para.SReplyPara
import cc.uling.usdk.board.wz.para.SSReplyPara
import cc.uling.usdk.constants.CodeUtil
import com.awish.machine.Option
import com.awish.machine.R
import com.awish.machine.ui.templates.LogEntry
import com.awish.machine.ui.templates.LogLevel
import com.awish.machine.ui.templates.Logger
import com.google.gson.Gson
import java.util.Locale

@Composable
fun ProductCard(option: Option, board: UBoard, openMenu: () -> Unit) {
  // NOTE:
  // In fact, Option should be obtained from the database and remain in memory all the time.
  var visibleOfProductModel by remember { mutableStateOf(false) }
  val context = LocalContext.current

  val runnable = remember { mutableStateMapOf<Int, Boolean>(option.code!! to false) }
  val badge: String = String.format(Locale.US, "A%02d", option.code?.plus(1))

  val logs = remember { mutableStateListOf<LogEntry>() }

  Box(
    modifier = Modifier
      .widthIn(min = 0.dp)
      .fillMaxWidth(0.48f) // 占 50%
  ) {
    ElevatedCard(
      colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
      ),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column {
        Image(
          painter = painterResource(R.drawable.sever_up),
          contentDescription = "Product Image",
          modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)),
          contentScale = ContentScale.Crop
        )

        Row(
          modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Column(
            modifier = Modifier
              .fillMaxHeight()
              .weight(1f),
            verticalArrangement = Arrangement.SpaceBetween
          ) {
            Text(
              text = stringResource(R.string.product_7up_summer_lemon_soda),
              style = MaterialTheme.typography.titleLarge,
              maxLines = 1
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
              text = stringResource(R.string.sugar_free_hydration_tip),
              style = MaterialTheme.typography.titleMedium,
              color = Color.Gray,
              overflow = TextOverflow.Ellipsis,
              maxLines = 3,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = buildAnnotatedString {
                  withStyle(
                    style = SpanStyle(
                      fontSize = MaterialTheme.typography.labelMedium.fontSize,
                      color = MaterialTheme.colorScheme.onSurface
                    )
                  ) {
                    append("$")
                  }
                  withStyle(
                    style = SpanStyle(
                      fontSize = MaterialTheme.typography.headlineLarge.fontSize,
                      color = Color.Red
                    )
                  ) {
                    append("99.0")
                  }
                }
              )

              Text(
                text = buildAnnotatedString {
                  withStyle(
                    style = SpanStyle(
                      fontSize = MaterialTheme.typography.labelMedium.fontSize,
                      color = MaterialTheme.colorScheme.onSurface
                    )
                  ) {
                    append("Inventory:")
                  }
                  withStyle(
                    style = SpanStyle(
                      fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                    )
                  ) {
                    append("12")
                  }
                }
              )
            }

            Spacer(modifier = Modifier.height(18.dp))

            val serial_port_not_open_please_configure =
              stringResource(R.string.serial_port_not_open_please_configure)
            Button(
              onClick = {
                if (!option.isOpened) {
                  Toast.makeText(
                    context,
                    serial_port_not_open_please_configure,
                    Toast.LENGTH_SHORT
                  ).show()
                  openMenu() // 唤醒父组件的菜单

                  return@Button
                }

                visibleOfProductModel = true
              },
              modifier = Modifier.fillMaxWidth(),
              contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
            ) {
              Text(stringResource(R.string.buy), style = MaterialTheme.typography.labelLarge)
            }
          }
        }
      }
    }
    // 🎯 左上角 Badge 角标
    Text(
      text = badge,
      color = Color.White,
      style = MaterialTheme.typography.labelSmall,
      modifier = Modifier
        .align(Alignment.TopStart)
        .background(Color.Red, RoundedCornerShape(topStart = 8.dp))
        .padding(horizontal = 8.dp, vertical = 4.dp)
    )
    // 热销
    if (option.code == 0) {
      Text(
        text = "热销",
        color = Color.White,
        style = MaterialTheme.typography.labelSmall,
        modifier = Modifier
          .align(Alignment.TopEnd)
          .background(Color.Blue, RoundedCornerShape(topEnd = 8.dp))
          .padding(horizontal = 8.dp, vertical = 4.dp)
      )
    }
  }

  if (visibleOfProductModel) {
    AlertDialog(
      modifier = Modifier
        .fillMaxWidth(1f),
      onDismissRequest = { },
      title = { Text(stringResource(R.string.product_7up_summer_lemon_soda)) },
      text = {
        Column {
          if (runnable[option.code] == false) {
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(12.dp))
                .padding(7.dp)
            ) {
              Image(
                painter = painterResource(R.drawable.sever_up),
                contentDescription = "Product Image",
                modifier = Modifier
                  .fillMaxSize()
                  .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
              )

              // 左上角角标
              Text(
                text = badge,
                color = Color.White,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier
                  .align(Alignment.TopStart)
                  .background(Color.Red, RoundedCornerShape(topStart = 8.dp))
                  .padding(horizontal = 8.dp, vertical = 4.dp)
              )
            }

            Spacer(modifier = Modifier.height(5.dp))

            Text(
              text = stringResource(R.string.sugar_free_hydration_tip),
              style = MaterialTheme.typography.titleMedium,
              color = Color.Gray,
              overflow = TextOverflow.Ellipsis,
              maxLines = 1,
            )

            Spacer(modifier = Modifier.height(8.dp))
          }

          val dispensing_initiated_successfully =
            stringResource(R.string.dispensing_initiated_successfully)
          val serial_port_not_open_please_configure = stringResource(
            R.string.serial_port_not_open_please_configure
          )
          val initiate_dispensing_failed_check_config = stringResource(
            R.string.initiate_dispensing_failed_check_config
          )
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp), // 控制按钮间距
            verticalAlignment = Alignment.CenterVertically
          ) {
            Button(
              onClick = {
                if (!board.EF_Opened()) {
                  Toast.makeText(
                    context,
                    serial_port_not_open_please_configure,
                    Toast.LENGTH_SHORT
                  ).show()
                  openMenu()
                  return@Button
                }

                SReplyPara(
                  option.addr,
                  option.code!!, option.floorType!!, option.isDc, option.isLp
                ).apply {
                  board.Shipment(this)
                }.apply {
                  if (!this.isOK) {
                    Toast.makeText(
                      context,
                      initiate_dispensing_failed_check_config,
                      Toast.LENGTH_SHORT
                    ).show()
                    return@Button
                  }
                }

                runnable[option.code as Int] = true
                Toast.makeText(
                  context,
                  dispensing_initiated_successfully,
                  Toast.LENGTH_SHORT
                ).show()

                logs.add(
                  LogEntry.new(
                    message = "Success: \nChannel Slot: ${option.code}\nFloor Type: ${option.floorType}\nisDc: ${option.isDc}\nisLp: ${option.isLp}",
                    info = LogLevel.SUCCESS
                  )
                )

              },
              modifier = Modifier.weight(1f),
              contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
            ) {
              Text(
                stringResource(R.string.initiate_dispensing),
                style = MaterialTheme.typography.labelLarge
              )
            }

            val query_dispensing_status_failed =
              stringResource(R.string.query_dispensing_status_failed)
            Button(
              onClick = {
                val para = SSReplyPara(
                  option.addr
                ).apply {
                  board.GetShipmentStatus(this)
                }.apply {
                  if (!this.isOK) {
                    Toast.makeText(
                      context,
                      query_dispensing_status_failed,
                      Toast.LENGTH_SHORT
                    ).show()
                    return@Button
                  }
                }
                val values: Map<String, Any> = mapOf(
                  "status_code" to para.runStatus,
                  "status_message" to CodeUtil.getXYStatusMsg(para.runStatus).replace("idle", ""),
                  "error_code" to para.faultCode,
                  "error_message" to CodeUtil.getFaultMsg(para.faultCode),
                )

                logs.add(
                  LogEntry.new(
                    message = "Result: \n${Gson().toJson(values)}",
                    info = LogLevel.INFO
                  )
                )
              },
              modifier = Modifier.weight(1f),
              contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
              enabled = runnable[option.code] == true
            ) {
              Text(
                stringResource(R.string.query_result),
                style = MaterialTheme.typography.labelLarge
              )
            }
          }

          if (runnable[option.code] == true) {
            Spacer(modifier = Modifier.height(8.dp)) // 可选间隔

            // 第二半部分
            Logger(logs) {
              logs.clear()
            }
          }

        }
      },
      confirmButton = {},
      dismissButton = {
        TextButton(
          onClick = {
            runnable[option.code as Int] = false
            visibleOfProductModel = false
            logs.clear()
          }
        ) {
          Text(text = stringResource(id = R.string.btn_cancel))
        }
      }
    )
  }
}
