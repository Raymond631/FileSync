package com.example.filesync.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonResponse {
    private int status;
    private Object data;

    public static CommonResponse success(Object data) {
        return new CommonResponse(200, data);
    }

    public static CommonResponse error(Object data) {
        return new CommonResponse(400, data);
    }
}
