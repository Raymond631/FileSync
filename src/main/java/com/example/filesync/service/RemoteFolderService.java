package com.example.filesync.service;

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
    public void setResp(Message<Boolean> resp);

    void addFolder(RemoteFolder folder) throws IOException;

    List<RemoteFolder> getFolders();

    void removeFolder(String folderId);

    boolean searchLocalFolder(String folderId);
}
