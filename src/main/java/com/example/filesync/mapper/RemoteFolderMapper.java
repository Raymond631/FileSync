package com.example.filesync.mapper;

import com.example.filesync.entity.RemoteFolder;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RemoteFolderMapper {

    List<RemoteFolder> selectFolders();

    void deleteFolder(String folderId);
}
