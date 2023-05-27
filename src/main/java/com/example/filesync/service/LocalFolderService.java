package com.example.filesync.service;

import com.example.filesync.entity.LocalFolder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @author Raymond Li
 * @create 2023-05-26 16:00
 * @description
 */
public interface LocalFolderService {
    Map<String, LocalDateTime> selectFileList(String folderPath);

    Map<String, LocalDateTime> scanDirectory(String folderPath);

    void addFolder(LocalFolder localFolder);

    void removeFolder(String folderId);

    List<LocalFolder> getFolders();
}
