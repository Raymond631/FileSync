package com.example.filesync.mapper;

import com.example.filesync.entity.LocalFolder;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @author Raymond Li
 * @create 2023-05-26 11:28
 * @description
 */
@Mapper
public interface LocalFolderMapper {

    @MapKey(value = "relative_path")
    Map<String, LocalDateTime> getFileList(@Param("folder_path") String folderPath);

    void insertFolder(LocalFolder localFolder);

    void deleteFolder(@Param("folderId") String folderId);

    List<LocalFolder> selectFolders();
}
