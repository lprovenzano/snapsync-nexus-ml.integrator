package com.snapsync.nexus.repository.rest.dto.payments;

import com.snapsync.nexus.entity.payments.Payment;
import com.snapsync.nexus.exception.validation.OrderNotFoundException;
import com.snapsync.nexus.exception.validation.PayerNotFoundException;
import com.snapsync.nexus.repository.rest.dto.DomainDTO;
import com.snapsync.nexus.repository.rest.dto.orders.OrderDTO;
import com.snapsync.nexus.repository.rest.dto.users.UserDTO;
import com.snapsync.nexus.utils.Util;
import lombok.*;

@Getter
@Setter
@Builder(setterPrefix = "set")
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO extends DomainDTO<Payment> {
    private OrderDTO order;
    private UserDTO payer;

    @Override
    protected void checker() {
        if (Util.isNull(this.getOrder()) || Util.isNull(this.getOrder().getId())) {
            throw new OrderNotFoundException();
        }
        if (Util.isNull(this.getPayer()) || Util.isNull(this.getPayer().getId())) {
            throw new PayerNotFoundException();
        }
    }

    @Override
    protected Payment mapper() {
        return Payment.builder()
                .setOrder(getOrder().map())
                .setPayer(getPayer().map())
                .build();
    }
}
