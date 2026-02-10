package com.ibosng.dbservice.services.history;

import com.ibosng.dbservice.entities.history.TeilnehmerHistory;
import com.ibosng.dbservice.services.BaseService;

import java.time.LocalDateTime;
import java.util.List;

public interface TeilnehmerHistoryService extends BaseService<TeilnehmerHistory> {

    List<TeilnehmerHistory> getAllForActionAfter(Character action, LocalDateTime actionTimestamp);
}
