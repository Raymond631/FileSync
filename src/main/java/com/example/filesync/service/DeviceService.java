package com.example.filesync.service;

import java.io.IOException;

/**
 * @author Raymond Li
 * @create 2023-05-26 19:25
 * @description
 */
public interface DeviceService {
    void addFolder(String deviceId,String folderId) throws IOException;
}
