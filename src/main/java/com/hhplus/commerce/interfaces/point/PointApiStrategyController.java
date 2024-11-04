package com.hhplus.commerce.interfaces.point;

import com.hhplus.commerce.application.point.PointChargeService;
import com.hhplus.commerce.application.point.dto.PointRequest;
import com.hhplus.commerce.common.response.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/points")
public class PointApiStrategyController {
    private final PointChargeService pointChargeService;

    @PostMapping("/charge/p")
    public CommonResponse chargePointPessimisticLock(
            @RequestHeader("customerId") Long customerId,
            @RequestBody PointRequest request
    ) {
        Long chargedPoint = pointChargeService.chargePointWithPessimisticLock(customerId, request);

        return CommonResponse.success(chargedPoint);
    }

    @PostMapping("/charge/o")
    public CommonResponse chargePointOptimisticLock(
            @RequestHeader("customerId") Long customerId,
            @RequestBody PointRequest request
    ) {
        Long chargedPoint = pointChargeService.chargePointWithOptimisticLock(customerId, request);

        return CommonResponse.success(chargedPoint);
    }

    @PostMapping("/charge/d")
    public CommonResponse chargePointDistributedLock(
            @RequestHeader("customerId") Long customerId,
            @RequestBody PointRequest request
    ) {
        Long chargedPoint = pointChargeService.chargePointWithDistributedLock(customerId, request);

        return CommonResponse.success(chargedPoint);
    }
}
