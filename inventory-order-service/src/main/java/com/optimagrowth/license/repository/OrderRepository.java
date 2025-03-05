package com.optimagrowth.license.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.optimagrowth.license.model.Order;

@Repository
public interface OrderRepository extends CrudRepository<Order, Long> {
	public List<Order> findByUserId(Long userId);
	public Order findByUserIdAndOrderId(Long userId, Long orderId);
}
