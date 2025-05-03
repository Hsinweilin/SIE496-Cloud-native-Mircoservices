package com.optimagrowth.license.service.client;

import com.optimagrowth.license.events.model.PaymentChangeModel;
import com.optimagrowth.license.model.Payment;
import com.optimagrowth.license.repository.PaymentRedisRepository;
import com.optimagrowth.license.utils.UserContext;
import com.optimagrowth.license.utils.UserContextHolder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Qualifier;


@Component
public class OrderServiceRestClient {

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceRestClient.class);

    @Autowired
    @Qualifier("getRestTemplate")
    private RestTemplate restTemplate;

    @Autowired
    private PaymentRedisRepository paymentRedisRepository;

    public Payment getPaymentFromCacheOrApi(PaymentChangeModel paymentChange) {
        Long paymentId = paymentChange.getPaymentId();
        logger.debug("📦 Fetching payment [{}] using correlation ID: {}", paymentId, UserContextHolder.getContext().getCorrelationId());


        Payment cachedPayment = checkRedisCache(paymentId);
        if (cachedPayment != null) {
            logger.debug("✅ Payment found in Redis: {}", cachedPayment);
            return cachedPayment;
        }

        logger.debug("🔍 Payment not in Redis, calling payment service via gateway");

        String url = "http://gateway:8072/payment-service/v1/payment/{paymentId}";
        HttpEntity<String> entity = new HttpEntity<>(null); // headers handled by UserContextInterceptor

        ResponseEntity<Payment> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                Payment.class,
                paymentId
        );

        Payment payment = response.getBody();
        if (payment != null) {
            cachePaymentObject(payment);
        }

        return payment;
    }

    private Payment checkRedisCache(Long paymentId) {
        try {
            return paymentRedisRepository.findById(paymentId.toString()).orElse(null);
        } catch (Exception e) {
            logger.error("❌ Failed to retrieve payment [{}] from Redis: {}", paymentId, e.getMessage());
            return null;
        }
    }

    private void cachePaymentObject(Payment payment) {
        try {
            paymentRedisRepository.save(payment);
            logger.debug("💾 Cached payment [{}] in Redis", payment.getPaymentId());
        } catch (Exception e) {
            logger.error("❌ Failed to cache payment [{}] in Redis: {}", payment.getPaymentId(), e.getMessage());
        }
    }
}
