package com.example.filesync.mapper;

import com.example.filesync.entity.LocalFolder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Raymond Li
 * @create 2023-05-26 11:28
 * @description
 */
@Mapper
public interface LocalFolderMapper {

    void insertFolder(LocalFolder localFolder);

    void deleteFolder(@Param("folderId") String folderId);

    List<LocalFolder> selectFolders();
}
