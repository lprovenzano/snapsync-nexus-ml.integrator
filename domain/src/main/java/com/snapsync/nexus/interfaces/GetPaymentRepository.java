package com.snapsync.nexus.interfaces;

import com.snapsync.nexus.entity.auth.Authorization;
import com.snapsync.nexus.entity.auth.Credential;
import com.snapsync.nexus.entity.payments.Payment;

public interface GetPaymentRepository {
    Payment execute(Long id, String token);
}
