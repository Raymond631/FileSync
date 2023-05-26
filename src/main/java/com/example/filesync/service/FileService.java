package com.example.filesync.service;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * @author Raymond Li
 * @create 2023-05-26 16:00
 * @description
 */
public interface FileService {
    Map<String, LocalDateTime> selectFileList(String folderPath);

    Map<String, LocalDateTime> scanDirectory(String folderPath);
}
