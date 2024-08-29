package miu.edu.orderservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "t-order-line-items")
public class OrderLineItems {

    @Id
    @GeneratedValue
    private Long id;
    private String skuCode;
    private BigDecimal price;
    private Integer quantity;

}
