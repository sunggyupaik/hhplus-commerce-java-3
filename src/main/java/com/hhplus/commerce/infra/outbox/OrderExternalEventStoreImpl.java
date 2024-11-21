package com.hhplus.commerce.infra.outbox;

import com.hhplus.commerce.domain.order.OrderExternalEventStore;
import com.hhplus.commerce.domain.outbox.Outbox;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderExternalEventStoreImpl implements OrderExternalEventStore {
    private final OutboxRepository outboxRepository;

    @Override
    public void save(Outbox outbox) {
        outboxRepository.save(outbox);
    }
}
