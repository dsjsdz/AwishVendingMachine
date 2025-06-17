package com.awish.machine

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuOpen
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import cc.uling.usdk.USDK
import cc.uling.usdk.board.wz.para.SVReplyPara
import cc.uling.usdk.constants.ErrorConst
import com.awish.machine.ui.components.AddrSelectorDialog
import com.awish.machine.ui.components.ChannelSelectorDialog
import com.awish.machine.ui.components.FloorTypeSelectorDialog
import com.awish.machine.ui.components.ProductCard
import com.awish.machine.ui.components.SerialPortSelectorDialog
import com.awish.machine.viewmodel.FloorTypeViewModel
import com.awish.machine.viewmodel.SerialPortViewModel
import kotlinx.coroutines.launch

data class Option(
  var commid: String?,
  var addr: Int,
  var isDc: Boolean,
  var isLp: Boolean,
  var code: Int?,
  var floorType: Int?,
  var isOpened: Boolean
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun Welcome(navController: NavHostController) {
  val context = LocalContext.current

  var option by remember {
    mutableStateOf(
      Option(
        commid = "/dev/ttyS1",
        addr = 1,
        isDc = false,
        isLp = false,
        code = 0,
        floorType = 1,
        isOpened = false
      )
    )
  }

  // awish vending machine driver config
  var board by remember { mutableStateOf(USDK.getInstance().create(option.commid)) }

  var visibleOfFloorTypeModel by remember { mutableStateOf(false) }
  var visibleOfAddrModel by remember { mutableStateOf(false) }
  var visibleOfChannelModel by remember { mutableStateOf(false) }
  var visibleOfSerialPortModel by remember { mutableStateOf(false) }

  val ftvm: FloorTypeViewModel = hiltViewModel()
  val spvm: SerialPortViewModel = hiltViewModel()
  val serialPaths: List<String> = spvm.serialPaths

  var expanded by remember { mutableStateOf(false) }

  val appName = stringResource(id = R.string.app_name)
  val appVersion = BuildConfig.VERSION_NAME
  val title = "$appName v$appVersion"

  val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
  val scope = rememberCoroutineScope()

  ModalNavigationDrawer(
    drawerState = drawerState,
    drawerContent = {
      ModalDrawerSheet {
        Text(
          text = title,
          modifier = Modifier.padding(16.dp),
          style = MaterialTheme.typography.titleMedium
        )
        HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)

        Column(
          verticalArrangement = Arrangement.spacedBy(12.dp), // 这行是关键
          modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
        ) {
          Text(
            text = stringResource(R.string.config_note_after_release),
            modifier = Modifier.padding(0.dp),
            style = MaterialTheme.typography.titleSmall,
            color = Color.Red
          )

          val please_close_serial_port_first: String =
            stringResource(R.string.please_close_serial_port_first)
          ExposedDropdownMenuBox(
            expanded = false,
            onExpandedChange = {
              if (option.isOpened) {
                Toast.makeText(
                  context,
                  please_close_serial_port_first,
                  Toast.LENGTH_SHORT
                ).show()
                return@ExposedDropdownMenuBox
              }
              visibleOfSerialPortModel = true
            },
            modifier = Modifier.fillMaxWidth()
          ) {
            OutlinedTextField(
              value = option.commid ?: stringResource(R.string.label_select),
              onValueChange = {},
              label = {
                Text(
                  if (option.commid != null) stringResource(R.string.serial_path) else stringResource(
                    R.string.select_serial_path
                  )
                )
              },
              readOnly = true,
              modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
              singleLine = true,
            )
          }
          ExposedDropdownMenuBox(
            expanded = false,
            onExpandedChange = {
              if (option.isOpened) {
                Toast.makeText(context, please_close_serial_port_first, Toast.LENGTH_SHORT).show()
                return@ExposedDropdownMenuBox
              }
              visibleOfAddrModel = true
            },
            modifier = Modifier.fillMaxWidth()
          ) {
            OutlinedTextField(
              value = "${option.addr}",
              onValueChange = { },
              label = {
                Text(
                  if (option.floorType != null) stringResource(R.string.select_addr) else stringResource(
                    R.string.select_addr
                  )
                )
              },
              readOnly = true,
              modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
              singleLine = true,
            )
          }
          ExposedDropdownMenuBox(
            expanded = false,
            onExpandedChange = {
              if (option.isOpened) {
                Toast.makeText(context, please_close_serial_port_first, Toast.LENGTH_SHORT).show()
                return@ExposedDropdownMenuBox
              }
              visibleOfFloorTypeModel = true
            },
            modifier = Modifier.fillMaxWidth()
          ) {
            OutlinedTextField(
              value = ftvm.options.find { it.id == option.floorType }?.title
                ?: stringResource(R.string.label_select),
              onValueChange = { },
              label = {
                Text(
                  if (option.floorType != null) stringResource(R.string.floor_type) else stringResource(
                    R.string.select_floor_type
                  )
                )
              },
              readOnly = true,
              modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
              singleLine = true,
            )
          }

          Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
          ) {
            Checkbox(
              checked = option.isDc,
              onCheckedChange = {
                option = option.copy(isDc = it)
              },
              enabled = !option.isOpened
            )
            Text(
              stringResource(R.string.is_dc)
            )
          }

          Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
          ) {
            Checkbox(
              checked = option.isLp,
              onCheckedChange = {
                option = option.copy(isLp = it)
              },
              enabled = !option.isOpened
            )
            Text(
              stringResource(R.string.is_lp)
            )
          }

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Button(
              onClick = {
                // 注意：实际开发过程中，只会出现一次初始化，后期都固定串口路径
                if (option.isOpened) {
                  board.let { it ->
                    it.EF_CloseDev()
                    option = option.copy(isOpened = false)
                  }
                }
                board = USDK.getInstance().create(option.commid)
                board.let { it ->
                  val resp = it.EF_OpenDev(option.commid, 9600)
                  var message = "打开串口失败，请重新选择"
                  if (resp == ErrorConst.MDB_ERR_NO_ERR) {
                    val para = SVReplyPara(option.addr)
                    it.GetSoftwareVersion(para)
                    if (para.isOK) {
                      option = option.copy(isOpened = true)
                      message = "成功打开串口"
                    } else {
                      message = "打开串口失败，请重新选择"
                    }
                  }

                  Toast.makeText(
                    context,
                    message,
                    Toast.LENGTH_SHORT
                  ).show()
                }
              },
              enabled = !option.isOpened
            ) {
              Text(stringResource(R.string.takes_effect_after_saving))
            }
            val serial_port_closed_successfully =
              stringResource(R.string.serial_port_closed_successfully)
            if (option.isOpened) {
              Button(
                onClick = {
                  if (option.isOpened) {
                    board.let { it ->
                      it.EF_CloseDev()
                      option = option.copy(isOpened = false)
                    }
                    Toast.makeText(
                      context,
                      serial_port_closed_successfully,
                      Toast.LENGTH_SHORT
                    ).show()
                  }
                },
              ) {
                Text(stringResource(R.string.close_serial_port))
              }
            }
          }
        }

        Spacer(modifier = Modifier.weight(1f))

        HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)
        NavigationDrawerItem(
          label = { Text(stringResource(id = R.string.nav_about)) },
          selected = false,
          onClick = {
            scope.launch { drawerState.close() }
            expanded = false
            navController.navigate("about")
          }
        )
      }
    }
  ) {
    Scaffold(
      topBar = {
        CenterAlignedTopAppBar(
          navigationIcon = {
            IconButton(onClick = {
              scope.launch {
                if (drawerState.isClosed) {
                  drawerState.open()
                } else {
                  drawerState.close()
                }
              }
            }) {
              Icon(
                imageVector = if (drawerState.isOpen) Icons.AutoMirrored.Filled.MenuOpen else Icons.Default.Menu,
                contentDescription = if (drawerState.isOpen) "Close menu" else "Open menu"
              )
            }
          },
          title = { Text("$appName v$appVersion") },
          colors = TopAppBarDefaults.topAppBarColors(
            navigationIconContentColor = Color.White, // 左边图标颜色
            containerColor = Color(0xFF424ea0), // 背景颜色
            titleContentColor = Color.White,     // 文本颜色
            actionIconContentColor = Color.White     // 右边图标颜色
          ),
          actions = {
            IconButton(onClick = { expanded = !expanded }) {
              Icon(Icons.Default.MoreVert, contentDescription = "More options")
            }
            DropdownMenu(
              expanded = expanded,
              onDismissRequest = { expanded = false }
            ) {
              DropdownMenuItem(
                text = { Text(text = stringResource(id = R.string.nav_about)) },
                onClick = {
                  expanded = false
                  navController.navigate("about")
                }
              )
            }
          }
        )
      }
    ) { innerPadding ->
      Column(
        modifier = Modifier
          .fillMaxSize()
      ) {
        // 第一半部分
        Column(
          modifier = Modifier
            .weight(1f)
            .fillMaxWidth()
            .padding(innerPadding)
            .padding(8.dp)
            .verticalScroll(rememberScrollState()),
          verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          if (visibleOfSerialPortModel) {
            SerialPortSelectorDialog(
              serialPaths = serialPaths,
              defaultValue = option.commid,
              onSelect = { selected ->
                option = option.copy(commid = selected)
                visibleOfSerialPortModel = false

              }
            )
          }

          if (visibleOfFloorTypeModel) {
            FloorTypeSelectorDialog(
              options = ftvm.options,
              defaultValue = option.floorType,
              onSelect = { selected ->
                option = option.copy(floorType = selected.id)
                visibleOfFloorTypeModel = false

              }
            )
          }

          if (visibleOfAddrModel) {
            AddrSelectorDialog(
              defaultValue = option.addr,
              onSelect = { selected ->
                option = option.copy(addr = selected)
                visibleOfAddrModel = false

              }
            )
          }

          if (visibleOfChannelModel) {
            ChannelSelectorDialog(
              defaultValue = option.code,
              onSelect = { selected ->
                option = option.copy(code = selected)
                visibleOfChannelModel = false

              }
            )
          }

          FlowRow(
            modifier = Modifier
              .fillMaxWidth()
              .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            maxItemsInEachRow = 2 // 控制每行最多放几个
          ) {
            (0..99).forEach { code ->
              ProductCard(option.copy(code = code), board, openMenu = {
                scope.launch {
                  drawerState.open()
                }
              })
            }
          }

        }
      }
    }
  }

}
