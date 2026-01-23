package com.example.myapplication

import java.io.Serializable
import kotlin.reflect.KProperty

class StorageEntity<T : Serializable>(val key:String) {
    private var value: T? = null

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T? {
        if(value==null){
            value = (SPUtils.getInstance(MyApplication.instance)!!.getObject(key)) as? T
        }
        return value
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, newValue: T?) {
        value = newValue
        SPUtils.getInstance(MyApplication.instance)!!.putObj(key, newValue!!)
    }
}