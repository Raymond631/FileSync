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

    private int type;
    private T data;
}
