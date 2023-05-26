package com.example.filesync.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Raymond Li
 * @create 2023-05-26 19:11
 * @description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Host {
    private String ip;
    private int port;
}
