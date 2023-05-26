package com.example.filesync.service.impl;

import com.example.filesync.mapper.FileMapper;
import com.example.filesync.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

/**
 * @author Raymond Li
 * @create 2023-05-26 16:00
 * @description
 */
@Service
public class FileServiceImpl implements FileService {
    @Autowired
    private FileMapper fileMapper;

    @Override
    public Map<String, LocalDateTime> selectFileList(String folderPath) {
        return fileMapper.getFileList(folderPath);
    }

    @Override
    public Map<String, LocalDateTime> scanDirectory(String folderPath) {
        File directory = new File(folderPath);
        String rootPath = new File(directory.getAbsolutePath()).getParent();

        Map<String, LocalDateTime> fileInfo = new HashMap<>();
        scanFilesWithRecursion(folderPath,rootPath,fileInfo);
        return fileInfo;
    }
    public void scanFilesWithRecursion(String folderPath, String rootPath, Map<String, LocalDateTime> fileInfo) {
        File directory = new File(folderPath);
        if(directory.isDirectory()){
            File[] fileList = directory.listFiles();
            if(fileList != null){
                for (File file : fileList) {
                    //如果当前是文件夹，进入递归扫描文件夹
                    if (file.isDirectory()) {
                        scanFilesWithRecursion(file.getAbsolutePath(), rootPath,fileInfo);
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

    public void test(String folderPath){
        // Map<String, LocalDateTime> newFileInfo = scanDirectory(folderPath);
        // if(newFileInfo.isEmpty()){
        //     System.out.println("空文件夹");
        //     return;
        // }
        //
        // Map<String, LocalDateTime> oldFileInfo = selectFileList(folderPath);
        // if(oldFileInfo.isEmpty()){
        //     // 新文件夹
        //     File directory = new File(folderPath);
        //     MyFolder folder = new MyFolder(UUID.randomUUID().toString(),directory.getName(),directory.getAbsolutePath());
        //     fileMapper.insertFolder(folder);
        //     for (Map.Entry<String, LocalDateTime> entry : newFileInfo.entrySet()) {
        //         MyFile file = new MyFile(UUID.randomUUID().toString(),entry.getKey(),entry.getValue());
        //         fileMapper.insertFile(file);
        //     }
        //     // Client.sendFile(newFileInfo);
        // }else{
        //
        // }
    }
}
