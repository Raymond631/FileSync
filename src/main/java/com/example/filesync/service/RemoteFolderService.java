package com.example.filesync.service;

import com.example.filesync.entity.LocalFolder;
import com.example.filesync.entity.Message;
import com.example.filesync.entity.RemoteFolder;

import java.io.IOException;
import java.util.List;

/**
 * @author Raymond Li
 * @create 2023-05-26 19:25
 * @description
 */
public interface RemoteFolderService {
    Boolean addFolder(RemoteFolder folder) throws IOException, InterruptedException;

    List<RemoteFolder> getFolders();

    void removeFolder(String folderId);

    LocalFolder searchLocalFolder(String folderId);

    void sync(String folderId) throws IOException, InterruptedException;
}
