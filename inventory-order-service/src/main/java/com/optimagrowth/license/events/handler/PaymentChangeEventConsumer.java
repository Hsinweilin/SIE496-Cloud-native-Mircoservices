// all the import
package com.optimagrowth.license.events.handler;
import com.optimagrowth.license.events.model.PaymentChangeModel;
import com.optimagrowth.license.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.messaging.MessageHeaders;
import org.springframework.util.MimeTypeUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.cloud.stream.annotation.EnableBinding;

@EnableBinding(Sink.class)
public class PaymentChangeEventConsumer {

	private static final Logger logger = LoggerFactory.getLogger(PaymentChangeEventConsumer.class);

	@Autowired
	private OrderService orderService;

	@StreamListener(Sink.INPUT)
	public void handlePaymentChange(@Payload PaymentChangeModel paymentChange) {
		logger.debug("🔔 Received payment change event: {}", paymentChange);

		Long orderId = paymentChange.getOrderId();
		String paymentStatus = paymentChange.getPaymentStatus();

		// Call service to handle business logic
		orderService.updateOrderStatus(orderId, "Complete");
	}
}

