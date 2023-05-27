package com.example.filesync.common;

import org.springframework.util.DigestUtils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

public class CommonUtils {
    public static String getMac() {
        String macString;
        try {
            NetworkInterface network = NetworkInterface.getByInetAddress(InetAddress.getLocalHost());
            byte[] mac = network.getHardwareAddress();
            System.out.print("Current MAC address : ");

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
}
