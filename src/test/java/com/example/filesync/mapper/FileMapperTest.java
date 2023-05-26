package com.example.filesync.mapper;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Raymond Li
 * @create 2023-05-26 11:33
 * @description
 */
@SpringBootTest
@RunWith(SpringRunner.class)
class FileMapperTest {
    @Autowired
    private FileMapper fileMapper;

    @Test
    void getFileList() {
        // System.out.println(fileMapper.getFileList());
    }
}