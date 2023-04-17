package com.catchyan.mrzz.model;

import lombok.Data;

import java.util.List;

@Data
public class CreateCompletionResponse {
    private Integer created;

    private Usage usage;

    private String model;

    private String id;

    /**
     * 回答列表
     */
    private List<ChoicesItem> choices;

    private String object;

    @Data
    public static class ChoicesItem {

        private String finishReason;

        private Integer index;

        private Message message;
    }

    @Data
    public static class Usage {

        private Integer completionTokens;

        private Integer promptTokens;

        private Integer totalTokens;
    }

    @Data
    public static class Message {
        private String role;
        private String content;
    }

}
