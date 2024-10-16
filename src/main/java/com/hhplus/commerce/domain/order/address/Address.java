package com.hhplus.commerce.domain.order.address;

import jakarta.persistence.Embeddable;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor
public class Address {
    private String receiverCity;
    private String receiverStreet;
    private String receiverZipcode;

    @Builder
    public Address(String receiverCity, String receiverStreet, String receiverZipcode) {
        this.receiverCity = receiverCity;
        this.receiverStreet = receiverStreet;
        this.receiverZipcode = receiverZipcode;
    }
}
