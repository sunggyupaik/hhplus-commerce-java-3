package com.hhplus.commerce.domain.point;

import com.hhplus.commerce.common.BaseTimeEntity;
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
}
