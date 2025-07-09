package com.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderItemDTO {
    private Long id;
    private Long productId;
    private Integer quantity;
    private BigDecimal unitPrice;
}
