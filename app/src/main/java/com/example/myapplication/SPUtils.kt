package com.example.myapplication

import android.content.Context
import android.content.SharedPreferences
import android.text.TextUtils
import android.util.Base64
import androidx.annotation.NonNull
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable
import java.util.Collections


/**
 * 作者：
 * 日期：2022/3/9 9:27
 * 注释：
 */
class SPUtils private constructor(context: Context, spName: String?, mode: Int) {

    private var sp: SharedPreferences? = null

    init {
        sp = context.applicationContext.getSharedPreferences(spName, mode)
    }


    /**
     * Return the single [SPUtils] instance
     *
     * @param spName The name of sp.
     * @param mode   Operating mode.
     * @return the single [SPUtils] instance
     */
    companion object {
        private val SP_UTILS_MAP: MutableMap<String?, SPUtils> = HashMap()
        @JvmOverloads
        fun getInstance(
            context: Context,
            spName: String = "",
            mode: Int = Context.MODE_PRIVATE
        ): SPUtils? {
            var spName = spName
            if (isSpace(spName)) spName = "spUtils"
            var spUtils: SPUtils? = SP_UTILS_MAP[spName]
            if (spUtils == null) {
                synchronized(SPUtils::class.java) {
                    spUtils = SP_UTILS_MAP[spName]
                    if (spUtils == null) {
                        spUtils = SPUtils(context, spName, mode)
                        spUtils?.let {
                            SP_UTILS_MAP[spName] = it
                        }
                    }
                }
            }
            return spUtils
        }

        fun getHistorySearchInfo(context: Context): SPUtils? {
            return getInstance(context, "history_search_info")
        }

        fun getSettings(context: Context):SPUtils?{
            return getInstance(context, "SP_SETTING")
        }


        private fun isSpace(s: String?): Boolean {
            if (s == null) return true
            var i = 0
            val len = s.length
            while (i < len) {
                if (!Character.isWhitespace(s[i])) {
                    return false
                }
                ++i
            }
            return true
        }

    }


    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     *
     * @param key
     * @param object
     */
   @JvmOverloads
    fun encode(key: String?, `object`: Any, isCommit: Boolean = false) {
        when (`object`) {
            is String -> {
                put(key, `object`, isCommit)
            }
            is Int -> {
                put(key, `object`, isCommit)
            }
            is Boolean -> {
                put(key, `object`, isCommit)
            }
            is Float -> {
                put(key, `object`, isCommit)
            }
            is Long -> {
                put(key, `object`, isCommit)
            }
            else -> {
                put(key, `object`.toString(), isCommit)
            }
        }
    }
    fun putObj(key: String, value: Serializable) {
        try {
            ByteArrayOutputStream().use { baos ->
                ObjectOutputStream(baos).use { objectOutputStream ->
                    objectOutputStream.writeObject(value)
                    val objStr = String(Base64.encode(baos.toByteArray(), Base64.DEFAULT))
                    put(key, objStr)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getObject(key: String): Any? {
        val str = getString(key)
        if (TextUtils.isEmpty(str)) {
            return null
        }
        val objBytes = Base64.decode(str, Base64.DEFAULT)
        try {
            ByteArrayInputStream(objBytes).use { bais ->
                ObjectInputStream(bais).use { ois ->
                    val obj = ois.readObject()
                    return obj
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }


    /**
     * Put the string value in sp.
     *
     * @param key      The key of sp.
     * @param value    The value of sp.
     * @param isCommit True to use [SharedPreferences.Editor.commit],
     * false to use [SharedPreferences.Editor.apply]
     */
    fun put(@NonNull key: String?, value: String?, isCommit: Boolean = false) {
        if (isCommit) {
            sp?.edit()?.putString(key, value)?.commit()
        } else {
            sp?.edit()?.putString(key, value)?.apply()
        }
    }


    /**
     * Return the string value in sp.
     *
     * @param key          The key of sp.
     * @param defaultValue The default value if the sp doesn't exist.
     * @return the string value if sp exists or `defaultValue` otherwise
     */
    fun getString(@NonNull key: String?, defaultValue: String = ""): String {
        return sp?.getString(key, defaultValue) ?: defaultValue
    }

    /**
     * Put the Int value in sp.
     *
     * @param key      The key of sp.
     * @param value    The value of sp.
     * @param isCommit True to use [SharedPreferences.Editor.commit],
     * false to use [SharedPreferences.Editor.apply]
     */
    fun put(@NonNull key: String?, value: Int, isCommit: Boolean = false) {
        if (isCommit) {
            sp?.edit()?.putInt(key, value)?.commit()
        } else {
            sp?.edit()?.putInt(key, value)?.apply()
        }
    }

    /**
     * Return the int value in sp.
     *
     * @param key          The key of sp.
     * @param defaultValue The default value if the sp doesn't exist.
     * @return the int value if sp exists or `defaultValue` otherwise
     */
    fun getInt(@NonNull key: String?, defaultValue: Int = -1): Int {
        return sp?.getInt(key, defaultValue) ?: defaultValue
    }


    /**
     * Put the long value in sp.
     *
     * @param key      The key of sp.
     * @param value    The value of sp.
     * @param isCommit True to use [SharedPreferences.Editor.commit],
     * false to use [SharedPreferences.Editor.apply]
     */
    fun put(@NonNull key: String?, value: Long, isCommit: Boolean = false) {
        if (isCommit) {
            sp?.edit()?.putLong(key, value)?.commit()
        } else {
            sp?.edit()?.putLong(key, value)?.apply()
        }
    }


    /**
     * Return the long value in sp.
     *
     * @param key          The key of sp.
     * @param defaultValue The default value if the sp doesn't exist.
     * @return the long value if sp exists or `defaultValue` otherwise
     */
    fun getLong(@NonNull key: String?, defaultValue: Long = -1L): Long {
        return sp?.getLong(key, defaultValue) ?: defaultValue
    }


    /**
     * Put the float value in sp.
     *
     * @param key      The key of sp.
     * @param value    The value of sp.
     * @param isCommit True to use [SharedPreferences.Editor.commit],
     * false to use [SharedPreferences.Editor.apply]
     */
    fun put(@NonNull key: String?, value: Float, isCommit: Boolean = false) {
        if (isCommit) {
            sp?.edit()?.putFloat(key, value)?.commit()
        } else {
            sp?.edit()?.putFloat(key, value)?.apply()
        }
    }


    /**
     * Return the float value in sp.
     *
     * @param key          The key of sp.
     * @param defaultValue The default value if the sp doesn't exist.
     * @return the float value if sp exists or `defaultValue` otherwise
     */
    fun getFloat(@NonNull key: String?, defaultValue: Float = -1f): Float {
        return sp?.getFloat(key, defaultValue) ?: defaultValue
    }


    /**
     * Put the boolean value in sp.
     *
     * @param key      The key of sp.
     * @param value    The value of sp.
     * @param isCommit True to use [SharedPreferences.Editor.commit],
     * false to use [SharedPreferences.Editor.apply]
     */
    fun put(@NonNull key: String?, value: Boolean, isCommit: Boolean = false) {
        if (isCommit) {
            sp?.edit()?.putBoolean(key, value)?.commit()
        } else {
            sp?.edit()?.putBoolean(key, value)?.apply()
        }
    }


    /**
     * Return the boolean value in sp.
     *
     * @param key          The key of sp.
     * @param defaultValue The default value if the sp doesn't exist.
     * @return the boolean value if sp exists or `defaultValue` otherwise
     */
    fun getBoolean(@NonNull key: String?, defaultValue: Boolean = false): Boolean {
        return sp?.getBoolean(key, defaultValue) ?: defaultValue
    }


    /**
     * Put the set of string value in sp.
     *
     * @param key      The key of sp.
     * @param value    The value of sp.
     * @param isCommit True to use [SharedPreferences.Editor.commit],
     * false to use [SharedPreferences.Editor.apply]
     */
    fun put(
        @NonNull key: String?,
        value: Set<String?> = Collections.emptySet<String>(),
        isCommit: Boolean = false
    ) {
        if (isCommit) {
            sp?.edit()?.putStringSet(key, value)?.commit()
        } else {
            sp?.edit()?.putStringSet(key, value)?.apply()
        }
    }


    /**
     * Return the set of string value in sp.
     *
     *
     *
     * @param key          The key of sp.
     * @param defaultValue The default value if the sp doesn't exist.
     * @return the set of string value if sp exists or `defaultValue` otherwise
     */
    fun getStringSet(
        @NonNull key: String?,
        defaultValue: Set<String?> = Collections.emptySet<String>()
    ): Set<String?> {
        return sp?.getStringSet(key, defaultValue) ?: defaultValue
    }


    /**
     * Return all values in sp.
     *
     * @return all values in sp
     */
    fun getAll(): Map<String?, *>? {
        return sp?.all
    }

    /**
     * Return whether the sp contains the preference.
     *
     * @param key The key of sp.
     * @return `true`: yes<br></br>`false`: no
     */
    operator fun contains(@NonNull key: String?): Boolean {
        return sp?.contains(key) ?: false
    }


    /**
     * Remove the preference in sp.
     *
     * @param key      The key of sp.
     * @param isCommit True to use [SharedPreferences.Editor.commit],
     * false to use [SharedPreferences.Editor.apply]
     */
    fun remove(@NonNull key: String?, isCommit: Boolean = false) {
        if (isCommit) {
            sp?.edit()?.remove(key)?.commit()
        } else {
            sp?.edit()?.remove(key)?.apply()
        }
    }


    /**
     * Remove all preferences in sp.
     *
     * @param isCommit True to use [SharedPreferences.Editor.commit],
     * false to use [SharedPreferences.Editor.apply]
     */
    fun clear(isCommit: Boolean = false) {
        if (isCommit) {
            sp?.edit()?.clear()?.commit()
        } else {
            sp?.edit()?.clear()?.apply()
        }
    }


}