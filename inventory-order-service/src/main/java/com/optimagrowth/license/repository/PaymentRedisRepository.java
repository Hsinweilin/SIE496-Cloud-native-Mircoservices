package com.optimagrowth.license.repository;

import com.optimagrowth.license.model.Payment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.optimagrowth.license.model.Payment;

@Repository
public interface PaymentRedisRepository extends CrudRepository<Payment, String> {
}
