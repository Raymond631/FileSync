package com.example.filesync.service.impl;

import com.example.filesync.common.CommonUtils;
import com.example.filesync.entity.FolderInfo;
import com.example.filesync.entity.LocalFolder;
import com.example.filesync.entity.Message;
import com.example.filesync.entity.RemoteFolder;
import com.example.filesync.mapper.RemoteFolderMapper;
import com.example.filesync.service.RemoteFolderService;
import com.example.filesync.socket.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @author Raymond Li
 * @create 2023-05-26 19:45
 * @description
 */
@Service
public class RemoteFolderServiceImpl implements RemoteFolderService {
    public static Message<String> resp = null;  //回调“信箱”

    @Autowired
    private RemoteFolderMapper remoteFolderMapper;

    @Override
    public Boolean addFolder(RemoteFolder folder) throws InterruptedException, IOException {
        Message<RemoteFolder> msg = new Message<>(Message.findRemoteFolder, folder, CommonUtils.getLocalHostExactAddress());
        Client.broadcast(msg);
        if (waitForResponse() && !resp.getData().equals("")) {  // 有回复且对方有这个文件夹
            String basePath = folder.getLocalPath();
            // TODO 前端传来的basePath必须以”/“结尾
            folder.setLocalPath(basePath + resp.getData());
            remoteFolderMapper.insertFolder(folder);
            resp = null;  // 重置“信箱”
            return true;
        } else {
            resp = null;  // 重置“信箱”
            return false;
        }
    }

    @Override
    public List<RemoteFolder> getFolders() {
        return remoteFolderMapper.selectFolders();
    }

    @Override
    public void removeFolder(String folderId) {
        remoteFolderMapper.deleteFolder(folderId);
    }

    @Override
    public LocalFolder searchLocalFolder(String folderId) {
        System.out.println(remoteFolderMapper.selectLocalFolderById(folderId));
        return remoteFolderMapper.selectLocalFolderById(folderId);
    }

    @Override
    public void sync(String folderId) throws IOException, InterruptedException {
        RemoteFolder folder = remoteFolderMapper.selectRemoteFolderById(folderId);
        Message<RemoteFolder> msg = new Message<>(Message.findRemoteFolder, folder, CommonUtils.getLocalHostExactAddress());
        Client.broadcast(msg);  // 广播寻址
        if (waitForResponse() && !resp.getData().equals("")) {  // 有回复且对方有这个文件夹
            String destIp = resp.getSrcIp();  // 获取对方ip
            resp = null;  // 重置“信箱”

            Map<String, LocalDateTime> localInfo = CommonUtils.scanDirectory(folder.getLocalPath());
            FolderInfo folderInfo = new FolderInfo(folder, localInfo);
            System.out.println("开始");
            Client.sendFileInfo(folderInfo, destIp);
            System.out.println("结束");
        } else {
            resp = null;  // 重置“信箱”
        }
    }

    public boolean waitForResponse() throws InterruptedException {
        long t1 = System.currentTimeMillis();
        while (resp == null) {
            long t2 = System.currentTimeMillis();
            if (t2 - t1 > 10000) {
                return false;  // 10秒超时
            }
            Thread.sleep(1000);  // 1秒检查一次”信箱“
        }
        return true;
    }
}
