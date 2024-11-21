package com.hhplus.commerce.domain.order;

import com.hhplus.commerce.domain.outbox.Outbox;

public interface OrderExternalEventStore {
    void save(Outbox outbox);
}
