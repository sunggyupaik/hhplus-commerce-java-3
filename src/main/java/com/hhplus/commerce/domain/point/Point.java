package com.hhplus.commerce.domain.point;

import com.hhplus.commerce.common.BaseTimeEntity;
import com.hhplus.commerce.common.exception.IllegalStatusException;
import com.hhplus.commerce.common.response.ErrorCode;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "points")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class Point extends BaseTimeEntity {
    public static final long MAX_POINT = 100000L;
    public static final long MIN_POINT = 0L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long customerId;

    private Long point;

    @Builder
    public Point(
            Long id,
            Long customerId,
            Long point
    ) {
        this.id = id;
        this.customerId = customerId;
        this.point = point;
    }

    public Long charge(Long amount) {
        this.point += amount;
        if (this.point > MAX_POINT) {
            throw new IllegalStatusException(ErrorCode.POINT_BALANCE_OVER);
        }

        return this.point;
    }

    public Long use(Long amount) {
        this.point -= amount;
        if (this.point < MIN_POINT) {
            throw new IllegalStatusException(ErrorCode.POINT_BALANCE_INSUFFICIENT);
        }

        return this.point;
    }
}
