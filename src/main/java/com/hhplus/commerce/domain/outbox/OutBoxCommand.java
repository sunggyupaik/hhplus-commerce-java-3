package com.hhplus.commerce.domain.outbox;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class OutBoxCommand {
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SaveRequest {
        private String domainName;
        private String topic;
        private String eventType;
        private String message;
        private boolean processed;

        public Outbox toEntity() {
            return Outbox.builder()
                    .domainName(domainName)
                    .topic(topic)
                    .eventType(eventType)
                    .message(message)
                    .processed(processed)
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FindAllRequest {
        private String topic;
        private boolean processed;

        public static FindAllRequest of(String topic, boolean processed) {
            return FindAllRequest.builder()
                    .topic(topic)
                    .processed(processed)
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FindRequest {
        private String topic;
        private String message;

        public static FindRequest of(String topic, String message) {
            return FindRequest.builder()
                    .topic(topic)
                    .message(message)
                    .build();
        }
    }
}
