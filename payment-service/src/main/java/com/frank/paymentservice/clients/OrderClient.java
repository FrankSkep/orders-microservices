package com.frank.paymentservice.clients;

import com.frank.paymentservice.config.FeignClientConfig;
import com.frank.paymentservice.dto.order.OrderDTO;
import com.frank.paymentservice.dto.order.OrderStatus;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "order-service", configuration = FeignClientConfig.class)
public interface OrderClient {

    @GetMapping("/api/orders/internal/{id}")
    OrderDTO getOrderById(@PathVariable("id") Long id);

    @PutMapping("/api/orders/internal/{id}/status")
    void updateOrderStatus(@PathVariable("id") Long id, @RequestBody OrderStatus status);
}
