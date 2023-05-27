package com.example.filesync.common;

import org.springframework.util.DigestUtils;

import java.net.*;
import java.util.Enumeration;

public class CommonUtils {
    public static String getMac() {
        String macString;
        try {
            NetworkInterface network = NetworkInterface.getByInetAddress(InetAddress.getLocalHost());
            byte[] mac = network.getHardwareAddress();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < mac.length; i++) {
                sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
            }
            macString = sb.toString();
        } catch (SocketException | UnknownHostException e) {
            throw new RuntimeException(e);
        }
        return DigestUtils.md5DigestAsHex(macString.getBytes());
    }

    public static String getLocalHostExactAddress() {
        try {
            Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress ip;
            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = allNetInterfaces.nextElement();
                // TODO 暂时通过名称是否包含VMware来排除虚拟机ip
                if (!netInterface.isLoopback() && !netInterface.isVirtual() && netInterface.isUp() && !netInterface.getDisplayName().contains("VMware")) {
                    Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
                    while (addresses.hasMoreElements()) {
                        ip = addresses.nextElement();
                        if (ip instanceof Inet4Address) {
                            return ip.getHostAddress();
                        }
                    }
                }
            }
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }

    }
}
