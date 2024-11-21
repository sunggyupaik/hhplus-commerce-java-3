package com.hhplus.commerce.application.order;

import com.hhplus.commerce.domain.order.OrderExternalEventReader;
import com.hhplus.commerce.domain.order.OrderExternalEventStore;
import com.hhplus.commerce.domain.outbox.OutBoxCommand;
import com.hhplus.commerce.domain.outbox.Outbox;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderExternalEventService {
    private final OrderExternalEventStore eventStore;
    private final OrderExternalEventReader eventReader;

    public void create(OutBoxCommand.SaveRequest request) {
        eventStore.save(request.toEntity());
    }

    public List<Outbox> findAll(OutBoxCommand.FindAllRequest request) {
        return eventReader.findAllNotProcessedByTopic(request);
    }

    public void changeToProcessedTrue(Outbox outbox) {
        outbox.changeToProcessedTrue();
    }

    public Outbox findOutbox(OutBoxCommand.FindRequest request) {
        return eventReader.findByTopicAndMessage(request);
    }
}
