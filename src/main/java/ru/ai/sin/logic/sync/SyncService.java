package ru.ai.sin.logic.sync;

import ru.ai.sin.logic.sync.dto.SyncDTO;
import ru.ai.sin.logic.sync.dto.SyncUpdateReq;

import java.util.UUID;

public interface SyncService {

    SyncDTO getByUserId(String userId);

    void update(UUID id, SyncUpdateReq req);
}
