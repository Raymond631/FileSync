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
}
