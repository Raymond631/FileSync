package com.example.filesync.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RemoteFolder {
    private String deviceId;
    private String folderId;
    private String localPath;
    private String lastSync;
}
