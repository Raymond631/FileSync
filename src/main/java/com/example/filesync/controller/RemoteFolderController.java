package com.example.filesync.controller;

import com.example.filesync.common.CommonResponse;
import com.example.filesync.entity.RemoteFolder;
import com.example.filesync.service.RemoteFolderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RestController
public class RemoteFolderController {
    @Autowired
    private RemoteFolderService remoteFolderService;

    @GetMapping("/remoteFolder")
    public CommonResponse getFolder() {
        log.info("获取远程同步文件夹列表  |  param: 无参数");
        return CommonResponse.success(remoteFolderService.getFolders());
    }

    @DeleteMapping("/remoteFolder")
    public CommonResponse removeFolder(@RequestParam String folderId) {
        log.info("移除远程同步文件夹  |  param: " + folderId);
        remoteFolderService.removeFolder(folderId);
        return CommonResponse.success("移除成功");
    }

    /**
     * remoteFolder中封装的是远程设备的id（非本机）
     */
    @PostMapping("/remoteFolder")
    public CommonResponse addFolder(@RequestBody RemoteFolder remoteFolder) throws IOException, InterruptedException {
        remoteFolder.setLastSync("未同步");
        log.info("添加远程同步文件夹  |  param: " + remoteFolder);
        if (remoteFolderService.addFolder(remoteFolder)) {
            return CommonResponse.success("添加成功");
        } else {
            return CommonResponse.success("未找到设备或文件夹，请检查输入是否有误");
        }
    }

    /**
     * 同步
     */
    @PutMapping("/remoteFolder")
    public CommonResponse sync(@RequestParam String folderId) throws IOException, InterruptedException {
        log.info("同步文件夹  |  param: " + folderId);
        remoteFolderService.sync(folderId);
        return CommonResponse.success("同步成功");
    }
}
