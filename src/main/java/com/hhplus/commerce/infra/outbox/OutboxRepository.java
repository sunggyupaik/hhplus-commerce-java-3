package com.hhplus.commerce.infra.outbox;

import com.hhplus.commerce.domain.outbox.Outbox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OutboxRepository extends JpaRepository<Outbox, Long> {
    @Query(value = "select o from Outbox o where o.topic = :topic and o.processed = :processed")
    List<Outbox> findAllNotProcessedByTopic(@Param("topic") String topic, @Param("processed") boolean processed);

    @Query(value = "select o from Outbox o where o.topic = :topic and o.message = :message")
    Optional<Outbox> findByTopicAndMessage(@Param("topic")String topic, @Param("message")String message);
}
