package org.oms.orderingmanagementsystem.entities;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class CartItemId implements Serializable {
    private Long cartId;
    private Long productId;
}
