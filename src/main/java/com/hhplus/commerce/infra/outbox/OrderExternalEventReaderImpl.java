package com.hhplus.commerce.infra.outbox;

import com.hhplus.commerce.domain.order.OrderExternalEventReader;
import com.hhplus.commerce.domain.outbox.OutBoxCommand;
import com.hhplus.commerce.domain.outbox.Outbox;
import com.hhplus.commerce.support.exception.EntityNotFoundException;
import com.hhplus.commerce.support.response.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderExternalEventReaderImpl implements OrderExternalEventReader {
    private final OutboxRepository outboxRepository;

    @Override
    public List<Outbox> findAllNotProcessedByTopic(OutBoxCommand.FindAllRequest request) {
        return outboxRepository.findAllNotProcessedByTopic(request.getTopic(), request.isProcessed());
    }

    @Override
    public Outbox findByTopicAndMessage(OutBoxCommand.FindRequest request) {
        return outboxRepository.findByTopicAndMessage(request.getTopic(), request.getMessage())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.OUTBOX_NOT_FOUND));
    }
}
