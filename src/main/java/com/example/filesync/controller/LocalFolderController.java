package com.example.filesync.controller;

import com.example.filesync.common.CommonResponse;
import com.example.filesync.entity.LocalFolder;
import com.example.filesync.service.LocalFolderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * @author Raymond Li
 * @create 2023-05-26 18:34
 * @description
 */

@Slf4j
@RestController
public class LocalFolderController {
    @Autowired
    private LocalFolderService localFolderService;

    /**
     * 添加本地共享文件夹
     */
    @PostMapping("/localFolder")
    public CommonResponse addFolder(@RequestBody String folderPath) {
        log.info("添加本地共享文件夹  |  param: " + folderPath);
        LocalFolder localFolder = new LocalFolder(UUID.randomUUID().toString(), folderPath);
        localFolderService.addFolder(localFolder);
        return CommonResponse.success("添加成功");
    }

    /**
     * 移除本地共享文件夹
     */
    @DeleteMapping("/localFolder")
    public CommonResponse removeFolder(@RequestBody String folderId) {
        log.info("移除本地共享文件夹  |  param: " + folderId);
        localFolderService.removeFolder(folderId);
        return CommonResponse.success("移除成功");
    }

    /**
     * 前端获取数据
     */
    @GetMapping("/localFolder")
    public CommonResponse getFolders() {
        log.info("获取本地共享文件夹列表   |  param: 无参数");
        return CommonResponse.success(localFolderService.getFolders());
    }
}
