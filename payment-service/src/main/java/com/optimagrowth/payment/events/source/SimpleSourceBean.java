package com.optimagrowth.payment.events.source;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.messaging.MessageHeaders;
import org.springframework.util.MimeTypeUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;



import com.optimagrowth.payment.events.model.PaymentChangeModel; // ✅
import com.optimagrowth.payment.utils.UserContext;

@Component
public class SimpleSourceBean {

    private final Source source;
    private static final Logger logger = LoggerFactory.getLogger(SimpleSourceBean.class);

    @Autowired
    public SimpleSourceBean(Source source) {
        this.source = source;
    }

    public void publishPaymentChange(Long paymentId, Long userId, LocalDateTime paymentDate, Long orderId, String paymentStatus) {
        logger.debug("📤 Sending Kafka message for Payment ID: {}", paymentId);

        PaymentChangeModel change = new PaymentChangeModel(
                paymentId,
                userId,
                paymentDate,
                orderId,
                paymentStatus
        );

        logger.info("📤 Object field check: paymentId={}, userId={}, paymentDate={}, orderId={}, paymentStatus={}",
                change.getPaymentId(), change.getUserId(), change.getPaymentDate(), change.getOrderId(), change.getPaymentStatus());

        try {
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(change);
            logger.info("📤 Serialized JSON to Kafka: {}", json);
        } catch (Exception e) {
            logger.error("❌ JSON serialization failed", e);
        }

        try {
            boolean sent = source.output().send(
                MessageBuilder.withPayload(change)
                        .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                        .build()
            );
        
            logger.info(sent ? "✅ Kafka message sent" : "❌ Kafka message send failed");
        } catch (Exception e) {
            logger.error("❌ Kafka send error", e);
        }
        
    }
}
