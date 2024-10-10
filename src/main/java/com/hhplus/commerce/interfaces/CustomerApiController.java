package com.hhplus.commerce.interfaces;

import com.hhplus.commerce.common.response.CommonResponse;
import com.hhplus.commerce.domain.customer.CustomerPointResponse;
import com.hhplus.commerce.domain.customer.PointChargeRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerApiController implements CustomerApiSpecification {
    @Override
    @GetMapping("")
    public CommonResponse getCustomerPoint(@RequestHeader("Authorization") Long customerId) {
        CustomerPointResponse customerPointResponse = CustomerPointResponse.builder()
                .customerId(customerId)
                .point(2500L)
                .build();

        return CommonResponse.success(customerPointResponse);
    }

    @Override
    @PostMapping("/points/charge")
    public CommonResponse chargeCustomerPoint(
            @RequestHeader("Authorization") Long customerId,
            @RequestBody PointChargeRequest request
    ) {
        CustomerPointResponse customerPointResponse = CustomerPointResponse.builder()
                .customerId(customerId)
                .point(request.getAmount())
                .build();

        return CommonResponse.success(customerPointResponse);
    }
}
