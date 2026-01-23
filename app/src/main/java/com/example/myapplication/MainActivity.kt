package com.example.myapplication

import android.Manifest
import android.app.AlertDialog
import android.app.KeyguardManager
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.provider.Settings.SettingNotFoundException
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.permissionx.guolindev.PermissionX
import com.permissionx.guolindev.callback.ForwardToSettingsCallback
import com.permissionx.guolindev.request.ForwardScope
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.Serializable
import java.time.Instant
import java.util.Date
import java.util.Locale


class MainActivity : FragmentActivity() {
    /**
     * 设备策略服务
     */
    private var dpm: DevicePolicyManager? = null
    data class TestData(val number: Int,val name:String): Serializable
    var testString: TestData? by StorageEntity<TestData>("test")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<SoundWavesView>(R.id.swv).start()
        val f = SimpleDateFormat("yyyy-MMM-dd ", Locale.CHINA)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val d = f.format(Date.from(Instant.now()))
            Log.i("tang__test",d)
            findViewById<TextView>(R.id.tv_date).setText(R.string.test)
        }
        findViewById<TextView>(R.id.tv_date).setOnClickListener{
            startActivity(Intent(this,RecycleViewActivity::class.java))
        }
        findViewById<TextView>(R.id.tv_test).text=testString?.name+testString?.number
        findViewById<View>(R.id.ll_test).setOnClickListener {
            val newN = (testString?.number?:0)+1
            testString= TestData(newN,"name")
            findViewById<TextView>(R.id.tv_test).text=testString?.name+testString?.number
        }
    }
    fun jacoco(view:View?){
	//生成jacoco报告
        JacocoHelper.generateEcFile(false,this)
    }



    /**
     * 用代码去开启管理员
     */
    fun openAdmin(view: View?) {

        // 创建一个Intent
//        val intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN)
//        // 我要激活谁
//        val mDeviceAdminSample = ComponentName(this, MyAdmin::class.java)
//        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mDeviceAdminSample)
//        // 劝说用户开启管理员权限
//        intent.putExtra(
//            DevicePolicyManager.EXTRA_ADD_EXPLANATION,
//            "哥们开启我可以一键锁屏，你的按钮就不会经常失灵"
//        )
//        startActivity(intent)
        startActivity(Intent(this, WebActivity::class.java))
    }

    /**
     * 一键锁屏
     */
    fun lockscreen(view: View?) {
        val who = ComponentName(this, MyAdmin::class.java)
        if (dpm!!.isAdminActive(who)) {
            dpm!!.lockNow() // 锁屏
            dpm!!.resetPassword("", 0) // 设置屏蔽密码
            // 清除Sdcard上的数据
            // dpm.wipeData(DevicePolicyManager.WIPE_EXTERNAL_STORAGE);
            // 恢复出厂设置
            // dpm.wipeData(0);
        } else {
            Toast.makeText(this, "还没有打开管理员权限", Toast.LENGTH_LONG).show()
            return
        }
    }

    fun upAppBright(v: View) {
        val window = getWindow();
        val lp = window.getAttributes();
        lp.screenBrightness = (getScreenBrightness() + 5) / 255.0f;
        window.setAttributes(lp);
    }

    fun lowAppBright(v: View) {
        val window = getWindow();
        val lp = window.getAttributes();
        lp.screenBrightness = (getScreenBrightness() - 5) / 255.0f;
        window.setAttributes(lp);
    }

    fun upSysBright(v: View) {
        if (Settings.System.canWrite(applicationContext)) {
            ModifySettingsScreenBrightness(this, getScreenBrightness() + 5)
        } else {
            allowModifySettings()
        }
    }

    fun lowSysBright(v: View) {
        if (Settings.System.canWrite(applicationContext)) {
            ModifySettingsScreenBrightness(this, getScreenBrightness() - 5)
        } else {
            allowModifySettings()
        }
    }

    /**
     * 3.关闭光感，设置手动调节背光模式
     *
     * SCREEN_BRIGHTNESS_MODE_AUTOMATIC 自动调节屏幕亮度模式值为1
     *
     * SCREEN_BRIGHTNESS_MODE_MANUAL 手动调节屏幕亮度模式值为0
     */
    fun setScreenManualMode(context: Context) {
        val contentResolver = context.contentResolver
        try {
            val mode = Settings.System.getInt(
                contentResolver,
                Settings.System.SCREEN_BRIGHTNESS_MODE
            )
            if (mode == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC) {
                Settings.System.putInt(
                    contentResolver,
                    Settings.System.SCREEN_BRIGHTNESS_MODE,
                    Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL
                )
            }
        } catch (e: SettingNotFoundException) {
            e.printStackTrace()
        }
    }

    /**
     * 5.修改Setting 中屏幕亮度值
     *
     * 修改Setting的值需要动态申请权限 <uses-permission android:name="android.permission.WRITE_SETTINGS"></uses-permission>
     */
    private fun ModifySettingsScreenBrightness(
        context: Context,
        birghtessValue: Int
    ) {
        // 首先需要设置为手动调节屏幕亮度模式

        val contentResolver = context.contentResolver
        Settings.System.putInt(
            contentResolver,
            Settings.System.SCREEN_BRIGHTNESS, birghtessValue
        )
    }

    /**
     * 卸载当前软件
     */
    fun uninstall(view: View?) {
        // 1.先清除管理员权限
        val mDeviceAdminSample = ComponentName(
            this,
            MyAdmin::class.java
        )
        dpm!!.removeActiveAdmin(mDeviceAdminSample)
        // 2.普通应用的卸载
        val intent = Intent()
        intent.setAction("android.intent.action.VIEW")
        intent.addCategory("android.intent.category.DEFAULT")
        intent.setData(Uri.parse("package:$packageName"))
        startActivity(intent)
    }

    fun getScreenBrightness(): Int {
        val contentResolver = getContentResolver();
        val defVal = 125;
        val b = Settings.System.getInt(
            contentResolver,
            Settings.System.SCREEN_BRIGHTNESS
        );
        Log.i("tang", "系统亮度：$b")
        return b
    }

    fun cmd(v: View) {
        playRunTime("echo 执行input 开始")
        playRunTime("input keyevent 4")
        playRunTime("echo 执行input 结束")
    }

    //执行
    @Throws(Exception::class)
    private fun playRunTime(cmd: String) {
        val p = Runtime.getRuntime().exec(cmd)
        p.waitFor()
        val `is` = p.inputStream
        val reader = BufferedReader(InputStreamReader(`is`))
        var line: String = ""
        while (reader.readLine()?.also { line = it } != null) {
            Log.i("tang", (line + "\n"))
        }
        p.waitFor()
        `is`.close()
        reader.close()
        p.destroy()
    }

    override fun onResume() {
        super.onResume()
        Log.i("tang",Build.MANUFACTURER+ Build.MODEL)
    }

    // 在类中声明传感器管理器和监听器
    private lateinit var sensorManager: SensorManager
    override fun onStop() {
        super.onStop()
        Handler().postDelayed({
            //注册sensor传感器监听，监听用户摇动手机
            // 初始化传感器管理器
            sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
            sensorManager.registerListener(
                object : SensorEventListener {
                    override fun onSensorChanged(event: SensorEvent?) {
                        Log.i("Sensor", "onSensorChanged: 传感器数据改变${event}")
                    }

                    override fun onAccuracyChanged(
                        sensor: Sensor?,
                        accuracy: Int
                    ) {
                        Log.i("Sensor", "onAccuracyChanged: 传感器数据改变${accuracy}")
                    }

                },
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_UI
            )
        },3000)
    }


    /**
     * 4.非系统签名应用，引导用户手动授权修改Settings 权限
     */
    private val REQUEST_CODE_WRITE_SETTINGS = 1000

    private fun allowModifySettings() {
        // Settings.System.canWrite(MainActivity.this)
        // 检测是否拥有写入系统 Settings 的权限
        if (!Settings.System.canWrite(this@MainActivity)) {
            val builder = AlertDialog.Builder(
                this,
                android.R.style.Theme_Material_Light_Dialog_Alert
            )
            builder.setTitle("请开启修改屏幕亮度权限")
            builder.setMessage("请点击允许开启")
            // 拒绝, 无法修改
            builder.setNegativeButton(
                "拒绝"
            ) { dialog, which ->
                Toast.makeText(
                    this@MainActivity,
                    "您已拒绝修系统Setting的屏幕亮度权限", Toast.LENGTH_SHORT
                )
                    .show()
            }
            builder.setPositiveButton(
                "去开启"
            ) { dialog, which -> // 打开允许修改Setting 权限的界面
                val intent = Intent(
                    Settings.ACTION_MANAGE_WRITE_SETTINGS, Uri
                        .parse(
                            "package:"
                                    + packageName
                        )
                )
                startActivityForResult(
                    intent,
                    REQUEST_CODE_WRITE_SETTINGS
                )
            }
            builder.setCancelable(false)
            builder.show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_WRITE_SETTINGS) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // Settings.System.canWrite方法检测授权结果
                if (Settings.System.canWrite(applicationContext)) {
                    Toast.makeText(
                        this, "已允许",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        this@MainActivity, "您已拒绝修系统Setting的屏幕亮度权限",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    fun disableKeyguard(view: View) {
        val km = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        km.isKeyguardLocked

    }

    fun permission(view: View) {
        PermissionX.init(this).permissions(Manifest.permission.CAMERA).explainReasonBeforeRequest()
            .onExplainRequestReason { scope, deniedList, isBefore ->
                if (isBefore)
                    scope.showRequestReasonDialog(deniedList, "必须要拍照权限", "确定", "取消")
            }.onForwardToSettings(object : ForwardToSettingsCallback {
            override fun onForwardToSettings(scope: ForwardScope, deniedList: MutableList<String>) {
                scope.showForwardToSettingsDialog(deniedList, "去设置", "确定", "取消")
            }

        })
            .request { allGranted, grantedList, deniedList ->
                Toast.makeText(
                    this,
                    "申请了",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    fun toServer(view: View) {
        startActivity(Intent(this, ServerActivity::class.java))
    }

    fun toClient(view: View) {
        startActivity(Intent(this, ClientActivity::class.java))
    }

    /**
     * 是否为澎湃系统
     *
     * @return true为澎湃系统
     */
    fun isHyperOs(): Boolean {
        return "Xiaomi".equals(Build.BRAND, ignoreCase = true) &&
                !TextUtils.isEmpty(getHyperVersion())
    }

    /**
     * 获取澎湃系统版本号
     *
     * @return 版本号
     */
    fun getHyperVersion(): String? {
        return getProp("ro.mi.os.version.name", "")
    }

    private fun getProp(property: String, defaultValue: String): String? {
        try {
            val spClz = Class.forName("android.os.SystemProperties")
            val method = spClz.getDeclaredMethod("get", String::class.java)
            val value = method.invoke(spClz, property) as String
            return if (TextUtils.isEmpty(value)) {
                defaultValue
            } else value
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        return defaultValue
    }

}
