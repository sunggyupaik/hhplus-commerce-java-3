package com.hhplus.commerce.domain.point.history;

import com.hhplus.commerce.common.BaseTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "point_histories")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PointHistory extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long customerId;

    private Long amount;

    @Enumerated(EnumType.STRING)
    private PointType type;

    @Builder
    public PointHistory(
            Long id,
            Long customerId,
            Long amount,
            PointType type
    ) {
        this.id = id;
        this.customerId = customerId;
        this.amount = amount;
        this.type = type;
    }
}
