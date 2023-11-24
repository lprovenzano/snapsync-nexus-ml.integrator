package com.snapsync.nexus.repository.rest.dto.orders;

import com.snapsync.nexus.entity.orders.Order;
import com.snapsync.nexus.repository.rest.dto.DomainDTO;
import lombok.*;

@Getter
@Setter
@Builder(setterPrefix = "set")
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO extends DomainDTO<Order> {
    private Long id;

    @Override
    protected void checker() {

    }

    @Override
    protected Order mapper() {
        return Order.builder()
                .setId(this.getId())
                .build();
    }
}
