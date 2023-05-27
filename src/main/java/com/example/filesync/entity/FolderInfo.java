package com.example.filesync.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FolderInfo {
    private RemoteFolder folder;
    private Map<String, LocalDateTime> info;
}
