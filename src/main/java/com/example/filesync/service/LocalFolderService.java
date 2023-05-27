package com.example.filesync.service;

import com.example.filesync.entity.LocalFolder;

import java.util.List;

/**
 * @author Raymond Li
 * @create 2023-05-26 16:00
 * @description
 */
public interface LocalFolderService {


    void addFolder(LocalFolder localFolder);

    void removeFolder(String folderId);

    List<LocalFolder> getFolders();
}
