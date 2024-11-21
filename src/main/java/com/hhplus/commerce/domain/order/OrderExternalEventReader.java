package com.hhplus.commerce.domain.order;

import com.hhplus.commerce.domain.outbox.OutBoxCommand;
import com.hhplus.commerce.domain.outbox.Outbox;

import java.util.List;

public interface OrderExternalEventReader {
    List<Outbox> findAllNotProcessedByTopic(OutBoxCommand.FindAllRequest request);

    Outbox findByTopicAndMessage(OutBoxCommand.FindRequest request);
}
