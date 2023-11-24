package com.snapsync.nexus.entity.payments;

import com.snapsync.nexus.entity.orders.Order;
import com.snapsync.nexus.entity.users.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(setterPrefix = "set")
public class Payment {
    private Order order;
    private User payer;
}
