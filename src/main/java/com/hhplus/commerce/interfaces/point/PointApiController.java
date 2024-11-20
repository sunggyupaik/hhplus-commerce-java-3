package com.hhplus.commerce.interfaces.point;

import com.hhplus.commerce.application.point.PointChargeService;
import com.hhplus.commerce.application.point.PointQueryService;
import com.hhplus.commerce.support.response.CommonResponse;
import com.hhplus.commerce.domain.point.PointCommand;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/points")
public class PointApiController implements PointApiSpecification {
    private final PointQueryService pointQueryService;
    private final PointChargeService pointChargeService;

    @GetMapping
    public CommonResponse getPoint(
            @RequestHeader("customerId") Long customerId
    ) {
        var pointInfo = pointQueryService.getPoint(customerId);
        PointDto.DetailResponse detailResponse = PointDto.DetailResponse.of(pointInfo);

        return CommonResponse.success(detailResponse);
    }

    @PostMapping("/charge")
    public CommonResponse chargePoint(
            @RequestHeader("customerId") Long customerId,
            @RequestBody @Valid PointDto.ChargeRequest request
    ) {
        var pointCommand = PointCommand.ChargeRequest.of(customerId, request);
        Long chargedPoint = pointChargeService.chargePointWithPessimisticLock(pointCommand);

        return CommonResponse.success(chargedPoint);
    }
}
