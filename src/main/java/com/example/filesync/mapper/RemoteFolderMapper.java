package com.example.filesync.mapper;

import com.example.filesync.entity.LocalFolder;
import com.example.filesync.entity.RemoteFolder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RemoteFolderMapper {

    List<RemoteFolder> selectFolders();

    void deleteFolder(@Param("folderId") String folderId);

    LocalFolder selectLocalFolderById(@Param("folderId") String folderId);

    int insertFolder(RemoteFolder folder);

    RemoteFolder selectRemoteFolderById(@Param("folderId") String folderId);
}
