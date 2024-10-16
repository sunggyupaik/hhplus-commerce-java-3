package com.hhplus.commerce.interfaces.point;

import com.hhplus.commerce.application.point.PointChargeService;
import com.hhplus.commerce.application.point.PointQueryService;
import com.hhplus.commerce.application.point.dto.PointChargeRequest;
import com.hhplus.commerce.common.response.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
//        PointResponse pointResponse = pointQueryService.getPoint(customerId);
//
//        return CommonResponse.success(pointResponse);
        return new PointApiSpecification.Fake().getPoint(customerId);
    }

    @PostMapping("/charge")
    public CommonResponse chargePoint(
            @RequestHeader("customerId") Long customerId,
            @RequestBody PointChargeRequest request
    ) {
//        Long charegdPoint = pointChargeService.chargePoint(customerId, request);
//
//        return CommonResponse.success(charegdPoint);
        return new PointApiSpecification.Fake().chargePoint(customerId, request);
    }
}
