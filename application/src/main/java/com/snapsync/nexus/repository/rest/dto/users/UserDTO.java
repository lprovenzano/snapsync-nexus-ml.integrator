package com.snapsync.nexus.repository.rest.dto.users;

import com.snapsync.nexus.entity.orders.Order;
import com.snapsync.nexus.entity.users.User;
import com.snapsync.nexus.repository.rest.dto.DomainDTO;
import lombok.*;

@Getter
@Setter
@Builder(setterPrefix = "set")
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO extends DomainDTO<User> {
    private Long id;

    @Override
    protected void checker() {

    }

    @Override
    protected User mapper() {
        return User.builder()
                .setId(this.getId())
                .build();
    }
}
