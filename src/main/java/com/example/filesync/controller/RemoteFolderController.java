package com.example.filesync.controller;

import com.example.filesync.common.CommonResponse;
import com.example.filesync.common.CommonUtils;
import com.example.filesync.service.RemoteFolderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class RemoteFolderController {
    @Autowired
    private RemoteFolderService remoteFolderService;

    @GetMapping("/deviceId")
    public CommonResponse getDeviceId() {
        return CommonResponse.success(CommonUtils.getMac());
    }

    /**
     * remoteFolder中封装的是远程设备的id（非本机）
     */
//    @PostMapping("/remoteFolder")
//    public CommonResponse addFolder(@RequestBody RemoteFolder RemoteFolder) {
//        log.info("添加远程同步文件夹  |  param: " + RemoteFolder.toString());
//        remoteFolderService.addFolder(RemoteFolder);
//    }
}
