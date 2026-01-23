package com.example.myapplication

import android.net.wifi.WifiManager
import android.os.Bundle
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.ServerSocket


class ServerActivity: FragmentActivity() {
    private var content=""
    private lateinit var tv:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_server)
        tv=findViewById(R.id.tv)
        getIp()
        Thread {
            startSocket()
        }.start()
    }
    private fun getIp(){
        val wm = getSystemService(WIFI_SERVICE) as WifiManager
        val ipAddressInt = wm.connectionInfo.ipAddress
        val ipAddress: String = java.lang.String.format(
            "%d.%d.%d.%d",
            ipAddressInt and 0xff,
            ipAddressInt shr 8 and 0xff,
            ipAddressInt shr 16 and 0xff,
            ipAddressInt shr 24 and 0xff
        )
        addContent("ip:$ipAddress")
    }
    private fun startSocket(){
        addContent("等待连接")
        try {
            var endFlag = false;
            val ss = ServerSocket(12345);
            while (!endFlag) {
                // 等待客户端连接
                val s = ss.accept();
                val input = BufferedReader(InputStreamReader(s.getInputStream()));
                //注意第二个参数据为true将会自动flush，否则需要需要手动操作output.flush()
                val output = PrintWriter(s.getOutputStream(),true);
                val message = input.readLine();
                addContent(message)
                output.println("message received! from server");
                //output.flush();
                if("shutDown".equals(message)){
                    endFlag=true;
                }
                s.close();
            }
            ss.close();

        } catch ( e:Exception) {
            addContent(e.toString())
        }
    }
    private fun addContent(s:String){
        runOnUiThread {
            content += s + "\n"
            tv.setText(content)
        }
    }
}