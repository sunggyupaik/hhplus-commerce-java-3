package com.hhplus.commerce.interfaces.point;

import com.hhplus.commerce.common.response.CommonResponse;
import com.hhplus.commerce.application.point.dto.PointResponse;
import com.hhplus.commerce.application.point.dto.PointChargeRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/points")
public class PointApiController implements PointApiSpecification {
    @Override
    @GetMapping
    public CommonResponse getPoint(
            @RequestHeader("customerId") Long customerId
    ) {
        PointResponse pointResponse = PointResponse.builder()
                .customerId(customerId)
                .point(2500L)
                .build();

        return CommonResponse.success(pointResponse);
    }

    @Override
    @PostMapping("//charge")
    public CommonResponse chargePoint(
            @RequestHeader("customerId") Long customerId,
            @RequestBody PointChargeRequest request
    ) {
        PointResponse pointResponse = PointResponse.builder()
                .customerId(customerId)
                .point(request.getAmount())
                .build();

        return CommonResponse.success(pointResponse);
    }
}
