package com.app.awish

import android.os.Build
import android.os.Bundle
import android.serialport.SerialPortFinder
import android.widget.Button
import android.widget.GridLayout
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.setMargins
import cc.uling.usdk.USDK
import cc.uling.usdk.board.UBoard
import cc.uling.usdk.board.mdb.para.MPReplyPara
import cc.uling.usdk.board.wz.para.HCReplyPara
import cc.uling.usdk.board.wz.para.SReplyPara
import cc.uling.usdk.board.wz.para.SSReplyPara
import cc.uling.usdk.board.wz.para.SVReplyPara
import cc.uling.usdk.constants.CodeUtil
import cc.uling.usdk.constants.ErrorConst
import com.google.gson.Gson
import kotlinx.coroutines.DelicateCoroutinesApi


data class Config(
    val sdk_version: String, // sdk version: 25
    val android_version: String, // android version, eg: 7.1.1
    val serial_sn: String, // sn: eg: af725
    val model_no: String, // no: eg: acx323
    val screen_width: Long?, // eg: 720
    val screen_height: Long?, // eg: 1080
    val commid: String?, // eg: /dev/ttyS0
    val baudrate: Long?,  // eg: 9600
    val status_bar_on: Long?, // status bar on: 0 or 1, notice: sqlite not support boolean
    val gesture_status_bar_on: Long?, // gesture status bar on: 0 or 1, notice: sqlite not support boolean
    val brightness: Long?, // brightness: 0-255
    val rows: Long?, // vending machine rows: 0-10
    val columns: Long?, // vending machine columns: 0-10
    val is_with_coin: Long?, // has coin reader, need mdb board
    val is_with_cash: Long?, // has cash reader, need mdb board
    val is_with_pos: Long?, // has pos reader, need mdb board
    val is_with_pulse: Long?, // has pulse reader, need mdb board
    val is_with_identify: Long?, // has identify reader, need mdb board
    val currency_code: String?, // currency code, need mdb board
    val currency_unit: Long?, // eg: 1, need mdb board
    val currency_decimal: Long?, // eg: 2, need mdb board
)

class MainActivity : AppCompatActivity() {
    // android device driver for screen device
    private var displayer = getAndroidDeviceDriver(Build.MODEL)
    /**
     * the driver of the board
     */
    private var baudrate: Int = 9600
    private lateinit var driver: UBoard
    private lateinit var serialsDevice: MutableList<String>
    private lateinit var config: Config

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // android device driver initialize event
        displayer.initialize(application.applicationContext)

        // init config or maybe your can load it from database or api
        this.initConfig()

        // initialization of the driver
        USDK.getInstance().init(application)

        // It is recommended to adopt this approach
        //        GlobalScope.launch(Dispatchers.IO) {
        //            async {
        //                initSerialDriver()
        //            }.await()
        //        }
        initSerialDriver()

        // set layout
        setContentView(R.layout.activity_main)

        val gridLayout = findViewById<GridLayout>(R.id.gridLayout)

        var selectedButton: Button? = null
        var code: Int? = null
        for (i in 0 until config.rows!!.toInt() * config.columns!!.toInt()) {
            val button = Button(this).apply {
                text = (i + 1).toString()
                layoutParams = GridLayout.LayoutParams().apply {
                    width = 0
                    height = GridLayout.LayoutParams.WRAP_CONTENT
                    columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                    rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                    setMargins(8)
                }
                setPadding(8, 8, 8, 8)

                setBackgroundColor(ContextCompat.getColor(context, R.color.default_gray))

                setOnClickListener {
                    selectedButton?.setBackgroundColor(
                        ContextCompat.getColor(context, R.color.default_gray)
                    )

                    setBackgroundColor(ContextCompat.getColor(context, R.color.blue_active))

                    selectedButton = this
                    code = i
                    Toast.makeText(application, "select button no: $text, the code is: $i", Toast.LENGTH_SHORT).show()
                }
            }
            gridLayout.addView(button)
        }

        var floorType: Int? = null
        val radioGroup = findViewById<RadioGroup>(R.id.radioGroup)
        findViewById<RadioGroup>(R.id.radioGroup).setOnCheckedChangeListener { _, checkedId ->
            floorType = when (checkedId) {
                R.id.caterpillar -> 1
                R.id.motor -> 2
                R.id.electromagnetic -> 3
                else -> null
            }
        }

        val tvSerialPort: TextView = findViewById(R.id.tv_serial_port)
        tvSerialPort.text = "Serial Path: ${config.commid}"

        val gson = Gson()
        val jsonData = gson.toJson(config)

        val jsonTextView = findViewById<TextView>(R.id.tv_json_data)
        jsonTextView.text = jsonData

        val btnExit: Button = findViewById(R.id.btn_exit)
        btnExit.setOnClickListener {
            finish()
        }

        var shipping: Boolean = false
        val btn1: Button = findViewById(R.id.btn1)
        btn1.setOnClickListener {
            if (code == null) {
                return@setOnClickListener Toast.makeText(this, "please choose channel code", Toast.LENGTH_SHORT).show()
            }

            if (floorType == null) {
                return@setOnClickListener Toast.makeText(this, "please choose floor type", Toast.LENGTH_SHORT).show()
            }

            // [Shipment document](https://www.awish.vip/docs/en-US/uboard/Shipment）
            val addr = 1;
            val isDc = false
            val isLp = false
            SReplyPara(
                addr,
                code!! % 100,
                floorType!!,
                isDc,
                isLp,
            ).apply {
                driver.Shipment(this)
            }.apply {
                if (!this.isOK) {
                    throw Exception("shipping failed")
                }
            }
            shipping = true
            Toast.makeText(this, "shipping success", Toast.LENGTH_SHORT).show()
        }

        // [get shipment status](https://www.awish.vip/docs/en-US/uboard/Shipment#getshipmentstatus)
        val btn2: Button = findViewById(R.id.btn2)
        btn2.setOnClickListener {
            if (!shipping) {
                return@setOnClickListener Toast.makeText(this, "please shipping", Toast.LENGTH_SHORT).show()
            }

            val addr = 1;
            val para = SSReplyPara(
                addr
            ).apply {
                driver.GetShipmentStatus(this)
            }.apply {
                if (!this.isOK) {
                    throw Exception("get shipment status failed")
                }
            }

            // [run Status](https://www.awish.vip/docs/en-US/uboard/Shipment#getshipmentstatus)
            val result: Map<String, Any> = mapOf(
                "status_code" to para.runStatus,
                "status_message" to CodeUtil.getXYStatusMsg(para.runStatus).replace("idle", ""),
                "error_code" to para.faultCode,
                "error_message" to CodeUtil.getFaultMsg(para.faultCode),
            )

            Toast.makeText(this, Gson().toJson(result), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        this.displayer.setStatusBar(true)
        this.displayer.setGestureStatusBar(true)
    }

    // initialization of the config
    private fun initConfig() {
        if (this::config.isInitialized) return
        this.config = Config(
            sdk_version = Build.VERSION.SDK_INT.toString(),
            android_version = Build.VERSION.RELEASE,
            serial_sn = this.displayer.getSerialNo(),
            model_no = Build.MODEL,
            screen_width = 0,
            screen_height = 0,
            baudrate = this.baudrate.toLong(),
            commid = "/dev/ttyS0",
            status_bar_on = 0,
            gesture_status_bar_on = 0,
            brightness = 255,
            rows = 0,
            columns = 0,
            is_with_identify = 0,
            is_with_cash = 0,
            is_with_coin = 0,
            is_with_pulse = 0,
            is_with_pos = 0,
            currency_code = "unknown",
            currency_unit = 1,
            currency_decimal = 2,
        )
    }

    /**
     * initialization of the driver
     */
    private fun initSerialDriver(addr: Int = 1) {
        val records = mutableListOf<String>()
        val paths = SerialPortFinder().allDevicesPath.sorted()
        try {
            paths.forEachIndexed { _, path ->
                val board = USDK.getInstance().create(path)
                board.let { it ->
                    val resp = it.EF_OpenDev(path, this.baudrate)
                    if (resp != ErrorConst.MDB_ERR_NO_ERR) {
                        records.add(path)
                        return@forEachIndexed
                    }

                    val para = SVReplyPara(addr)
                    it.GetSoftwareVersion(para)
                    if (para.isOK && !this::driver.isInitialized) {
                        this.driver = it
                        this.config = this.config.copy(commid = path)
                        Toast.makeText(this, "Serial port opened successfully. Path: $path ", Toast.LENGTH_SHORT).show()
                    }
                    records.add(path)
                }
            }

            this.serialsDevice = records

            // 读取驱动版 xy轴数量
            if (this::driver.isInitialized) {
                HCReplyPara(addr).apply {
                    driver.ReadHardwareConfig(this)
                }.apply {
                    if (this.isOK) {
                        config = config.copy(
                            rows = this.row.toLong(),
                            columns = this.column.toLong()
                        )
                    }
                }

                // 获取mdb配置
                cc.uling.usdk.board.mdb.para.HCReplyPara().apply {
                    driver.readHardwareConfig(this)
                }.let {
                    if (it.isOK) {
                        this.config = this.config.copy(
                            is_with_coin = if (it.isWithCoin) 1 else 0,
                            is_with_cash = if (it.isWithCash) 1 else 0,
                            is_with_pos = if (it.isWithPOS) 1 else 0,
                            is_with_pulse = if (it.isWithPulse) 1 else 0,
                            is_with_identify = if (it.isWithIdentify) 1 else 0,
                            currency_code = it.code,
                        )
                    }
                }

                // 读取外设支持最小面额
                MPReplyPara().apply {
                    driver.getMinPayoutAmount(this)
                }.let {
                    if (it.isOK) {
                        this.config = this.config.copy(
                            currency_unit = it.value.toLong(),
                            currency_decimal = it.decimal.toLong()
                        )
                    }
                }
            }
        } catch (e: Exception) {
            Toast.makeText(application, e.message, Toast.LENGTH_SHORT).show()
            throw e
        }
    }
}
