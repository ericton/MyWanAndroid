package com.example.myapplication

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket

class ClientActivity:FragmentActivity() {
    private lateinit var tv:TextView;
    private var content=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client)
        tv=findViewById(R.id.tv)
        findViewById<View>(R.id.bt_start).setOnClickListener{
            Thread {
                startSocket(findViewById<EditText>(R.id.et_ip).text.toString())
            }.start()
        }
    }
    private fun startSocket(ip:String){
        try {
            val s = Socket(ip, 12345);
            // outgoing stream redirect to socket
            val out = s.getOutputStream();
            // 注意第二个参数据为true将会自动flush，否则需要需要手动操作out.flush()
            val output = PrintWriter(out, true);
            output.println("Hello from client!");
            val input = BufferedReader(
                InputStreamReader(s
                .getInputStream())
            );
            // read line(s)
            val message = input.readLine();
            addContent(message)
            s.close();

        } catch (e:Exception) {
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