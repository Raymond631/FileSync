package com.example.filesync.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Raymond Li
 * @create 2023-05-26 20:12
 * @description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Folder {
    private String folderPath;
    private String deviceId;
}
