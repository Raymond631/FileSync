package com.example.filesync.service;

import com.example.filesync.entity.RemoteFolder;

import java.io.IOException;

/**
 * @author Raymond Li
 * @create 2023-05-26 19:25
 * @description
 */
public interface RemoteFolderService {
    void addFolder(RemoteFolder folder) throws IOException;
}
