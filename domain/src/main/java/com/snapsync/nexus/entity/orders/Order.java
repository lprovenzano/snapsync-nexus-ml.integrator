package com.snapsync.nexus.entity.orders;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(setterPrefix = "set")
public class Order {
    private Long id;
}
