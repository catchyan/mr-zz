package com.catchyan.mrzz.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
public class CreateCompletionRequest {
    /**
     * 模型
     */
    private String model;

    /**
     * 提示词
     */
    private List<Message> messages;

    private Integer temperature;

    private Integer top_p;

    private Integer n;

    private Boolean stream;

    private String stop;

    @Data
    @AllArgsConstructor
    public static class Message{
        private String role;
        private String content;
    }
}
