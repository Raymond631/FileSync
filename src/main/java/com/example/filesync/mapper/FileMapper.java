package com.example.filesync.mapper;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * @author Raymond Li
 * @create 2023-05-26 11:28
 * @description
 */
@Mapper
public interface FileMapper {

    @MapKey(value = "relative_path")
    Map<String, LocalDateTime> getFileList(@Param("folder_path") String folderPath);

}
