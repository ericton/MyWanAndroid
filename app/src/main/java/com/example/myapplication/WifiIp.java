package com.example.myapplication;


import android.util.Log;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class WifiIp {
    public static String TAG = "WifiIp";
    /**
     * 获取连接到手机热点上的设备信息
     * @return
     */
    public List<HashMap> getConnectedApInfo() {
        List<HashMap> connectedApInfo = new ArrayList<>();

        try {
            BufferedReader br = new BufferedReader(new FileReader("/proc/net/arp"));
            String line;

            while ((line = br.readLine()) != null) {
                /**
                 * 获取到的数组结果，示例：[192.168.227.138, 0x1, 0x2, 82:64:5e:01:49:fc, *, wlan2]
                 */
                String[] splitted = line.split(" +");
                HashMap hashMap = new HashMap();

                //设备信息判断标准
                if (splitted.length >= 4 && splitted[3].contains(":")) {
                    String ip = splitted[0];          //获取IP地址信息，代替设备名称
                    String address = splitted[3];     //获取Mac地址信息

                    hashMap.put("name", ip);
                    hashMap.put("address", address);

                    connectedApInfo.add(hashMap);
                    Log.d(TAG, "getConnectedApInfo()，获取连接到手机热点上的设备信息：" + Arrays.toString(splitted) + "    connectedApInfo：" + connectedApInfo.size() + "  " + connectedApInfo);
                }
            }
        } catch (Exception e) {
            Log.i(TAG,e.toString());
        }

        return connectedApInfo;
    }
}
