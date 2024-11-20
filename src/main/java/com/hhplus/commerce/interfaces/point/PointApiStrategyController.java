package com.hhplus.commerce.interfaces.point;

import com.hhplus.commerce.application.point.PointChargeService;
import com.hhplus.commerce.support.response.CommonResponse;
import com.hhplus.commerce.domain.point.PointCommand;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/points")
public class PointApiStrategyController {
    private final PointChargeService pointChargeService;

    @PostMapping("/charge/p")
    public CommonResponse chargePointPessimisticLock(
            @RequestHeader("customerId") Long customerId,
            @RequestBody @Valid PointDto.ChargeRequest request
    ) {
        var command = PointCommand.ChargeRequest.of(customerId, request);
        Long chargedPoint = pointChargeService.chargePointWithPessimisticLock(command);

        return CommonResponse.success(chargedPoint);
    }

    @PostMapping("/charge/o")
    public CommonResponse chargePointOptimisticLock(
            @RequestHeader("customerId") Long customerId,
            @RequestBody @Valid PointDto.ChargeRequest request
    ) {
        var command = PointCommand.ChargeRequest.of(customerId, request);
        Long chargedPoint = pointChargeService.chargePointWithOptimisticLock(command);

        return CommonResponse.success(chargedPoint);
    }

    @PostMapping("/charge/d")
    public CommonResponse chargePointDistributedLock(
            @RequestHeader("customerId") Long customerId,
            @RequestBody @Valid PointDto.ChargeRequest request
    ) {
        var command = PointCommand.ChargeRequest.of(customerId, request);
        Long chargedPoint = pointChargeService.chargePointWithDistributedLock(command);

        return CommonResponse.success(chargedPoint);
    }
}
