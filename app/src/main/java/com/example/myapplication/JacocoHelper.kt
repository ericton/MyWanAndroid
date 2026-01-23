package com.example.myapplication

import android.content.Context
import android.os.Environment
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream

/**
 * @ClassName JacocoHelper
 * @Author 张旭-202412311
 * @Date 2025/3/3
 * @Description:
 */
object JacocoHelper {
    private const val TAG = "JacocoHelper"

    /**
     * 生成ec文件
     *
     * @param isNew 是否重新创建ec文件
     */
    @JvmStatic
    fun generateEcFile(isNew: Boolean,context: Context) {

        var out: OutputStream? = null
        val mCoverageFilePath = File("${context.getExternalFilesDir("test")!!.path}/coverage.ec")

        try {
            if (isNew && mCoverageFilePath.exists()) {
                Log.d(TAG, "清除旧的ec文件")
                mCoverageFilePath.delete()
            }
            if (!mCoverageFilePath.exists()) {
                Log.d(TAG, "新建ec文件")
                mCoverageFilePath.createNewFile()

            }
            out = FileOutputStream(mCoverageFilePath.path, true)
//            Agent.getInstance(AgentOptions())s
            val agent = Class.forName("org.jacoco.agent.rt.RT")
                .getMethod("getAgent")
                .invoke(null)
            if (agent != null) {
                out.write(agent.javaClass.getMethod("getExecutionData", Boolean::class.javaPrimitiveType)
                    .invoke(agent, false) as ByteArray)
            }
        } catch (e: Exception) {
            Log.d(TAG, e.toString())
        } finally {
            try {
                out?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}