package com.example.filesync.service.impl;

import com.example.filesync.entity.LocalFolder;
import com.example.filesync.mapper.LocalFolderMapper;
import com.example.filesync.service.LocalFolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Raymond Li
 * @create 2023-05-26 16:00
 * @description
 */
@Service
public class LocalFolderServiceImpl implements LocalFolderService {
    @Autowired
    private LocalFolderMapper localFolderMapper;

    @Override
    public void addFolder(LocalFolder localFolder) {
        localFolderMapper.insertFolder(localFolder);
    }

    @Override
    public void removeFolder(String folderId) {
        localFolderMapper.deleteFolder(folderId);
    }

    @Override
    public List<LocalFolder> getFolders() {
        return localFolderMapper.selectFolders();
    }


    public void test(String folderPath) {
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
