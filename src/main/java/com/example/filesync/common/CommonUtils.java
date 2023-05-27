package com.example.filesync.common;

import org.springframework.util.DigestUtils;

import java.io.File;
import java.net.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

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
        throw new RuntimeException("无法解析本机ip");
    }

    public static Map<String, LocalDateTime> scanDirectory(String folderPath) {
        File directory = new File(folderPath);
        String rootPath = new File(directory.getAbsolutePath()).getParent();

        Map<String, LocalDateTime> fileInfo = new HashMap<>();
        scanFilesWithRecursion(folderPath, rootPath, fileInfo);
        return fileInfo;
    }

    public static void scanFilesWithRecursion(String folderPath, String rootPath, Map<String, LocalDateTime> fileInfo) {
        File directory = new File(folderPath);
        if (directory.isDirectory()) {
            File[] fileList = directory.listFiles();
            if (fileList != null) {
                for (File file : fileList) {
                    //如果当前是文件夹，进入递归扫描文件夹
                    if (file.isDirectory()) {
                        scanFilesWithRecursion(file.getAbsolutePath(), rootPath, fileInfo);
                    }
                    //非文件夹，将文件及其修改时间放进map
                    else {
                        Instant instant = Instant.ofEpochMilli(file.lastModified());
                        ZoneId zone = ZoneId.systemDefault();
                        LocalDateTime lastModified = LocalDateTime.ofInstant(instant, zone);

                        String absolutePath = file.getAbsolutePath();
                        String relativePath = absolutePath.replace(rootPath, "");
                        System.out.println("relativePath:" + relativePath);

                        fileInfo.put(relativePath, lastModified);
                    }
                }
            }
        }
    }
}
