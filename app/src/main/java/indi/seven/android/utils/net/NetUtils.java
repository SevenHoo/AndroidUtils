package indi.seven.android.utils.net;

import android.util.Log;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * Description: Functions related to Net. <br/>
 * Date: 2016/12/4 <br/>
 * @author mr.hoo7793@gmail.com
 */

public class NetUtils {

    private static final String TAG = NetUtils.class.getSimpleName();

    public static String[] getLocalIpAddress() {
        String[] ipList = new String[2];
        int intfNum = 0;
        int inetAddressNum = 0;
        int ipIndex = 0;
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                intfNum++;
                Log.d(TAG, "NetworkInterface " + intfNum);
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    inetAddressNum++;
                    Log.d(TAG, inetAddressNum + " ip : "
                            + inetAddress.getHostAddress().toString());
                    // filter ipv4 address.
                    if (!inetAddress.isLoopbackAddress()) {
                        String ip = inetAddress.getHostAddress().toString();
                        String[] ips = ip.split("\\.");
                        if (ips.length == 4) {
                            if (ip != null) {
                                ip = ip.substring(ip.lastIndexOf(".") + 1);
                            }
                            ipList[ipIndex] = ip;
                            ipIndex++;
                        }
                        ips = null;
                        ip = null;
                    }
                    inetAddress = null;
                }
                intf = null;
                return ipList;
            }
            return null;
        } catch (Exception e) {
            // TODO: handle exception
            return null;
        }
    }
}
