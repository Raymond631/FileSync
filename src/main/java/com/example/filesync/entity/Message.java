package com.example.filesync.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用于UDP-socket通信
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message<T> {
    public static final int findRemoteFolder = 1;
    public static final int findRemoteFolderResponse = 2;
    public static final int syncRequest = 3;

    private int type;
    private T data;
    private String srcIp;
}
