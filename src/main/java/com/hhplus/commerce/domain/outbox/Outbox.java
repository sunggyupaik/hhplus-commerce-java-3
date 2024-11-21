package com.hhplus.commerce.domain.outbox;

import com.hhplus.commerce.support.BaseTimeEntity;
import com.hhplus.commerce.support.exception.IllegalStatusException;
import com.hhplus.commerce.support.response.ErrorCode;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "outbox")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class Outbox extends BaseTimeEntity {
    public static final int PROCESSED_CHECK_MINUTE = 15;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 발신자 order
    private String domainName;

    //수신자 order-data-platform-event-v1
    private String topic;

    //제목 orderDataPlatform
    private String eventType;

    //내용 json형태 같은 것
    private String message;

    //발송완료 false
    private boolean processed = false;

    @Builder
    public Outbox(
            Long id,
            String domainName,
            String topic,
            String eventType,
            String message,
            boolean processed
    ) {
        this.id = id;
        this.domainName = domainName;
        this.topic = topic;
        this.eventType = eventType;
        this.message = message;
        this.processed = processed;
    }

    public void changeToProcessedTrue() {
        if (processed) {
            throw new IllegalStatusException(ErrorCode.COMMON_ILLEGAL_STATUS);
        }

        processed = true;
    }
}
