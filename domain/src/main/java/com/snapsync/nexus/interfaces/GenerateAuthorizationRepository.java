package com.snapsync.nexus.interfaces;

import com.snapsync.nexus.entity.auth.Authorization;
import com.snapsync.nexus.entity.auth.Credential;

public interface GenerateAuthorizationRepository {
    Authorization execute(Credential credential);
}
