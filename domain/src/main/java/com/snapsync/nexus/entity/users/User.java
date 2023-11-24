package com.snapsync.nexus.entity.users;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(setterPrefix = "set")
public class User {
    private Long id;
}
