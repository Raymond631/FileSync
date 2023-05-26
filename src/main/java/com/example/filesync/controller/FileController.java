package com.example.filesync.controller;

import com.example.filesync.service.DeviceService;
import com.example.filesync.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * @author Raymond Li
 * @create 2023-05-26 18:34
 * @description
 */
@RestController
public class FileController {
    @Autowired
    private FileService fileService;

    @Autowired
    private DeviceService deviceService;

    /**
     * 添加新的同步文件夹
     */
    @PostMapping("/addFolder")
    public void addFolder(String deviceId, String folderId){
        try {
            deviceService.addFolder(deviceId,folderId);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * TODO 定时拉取由前端实现
     */
    @GetMapping("/pull/{folderPath}")
    public void pull(@PathVariable String folderPath){
        Map<String, LocalDateTime> fileInfo = fileService.scanDirectory(folderPath);
        // deviceService.broadcast();
    }
}
