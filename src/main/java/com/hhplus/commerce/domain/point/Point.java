package com.hhplus.commerce.domain.point;

import com.hhplus.commerce.common.BaseTimeEntity;
import com.hhplus.commerce.common.exception.IllegalStatusException;
import com.hhplus.commerce.common.response.ErrorCode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "points")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Point extends BaseTimeEntity {
    public static final long MAX_POINT = 100000L;

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
}
