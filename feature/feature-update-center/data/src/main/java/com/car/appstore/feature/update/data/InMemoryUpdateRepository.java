package com.car.appstore.feature.update.data;

import com.car.appstore.core.domain.AppId;
import com.car.appstore.core.domain.DomainError;
import com.car.appstore.core.domain.DomainResult;
import com.car.appstore.core.domain.InstallTask;
import com.car.appstore.core.domain.InstallTaskStatus;
import com.car.appstore.core.domain.UpdateInfo;
import com.car.appstore.feature.update.domain.UpdateRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InMemoryUpdateRepository implements UpdateRepository {
    private final Map<AppId, UpdateInfo> updateMap;

    public InMemoryUpdateRepository(List<UpdateInfo> updates) {
        this.updateMap = new HashMap<>();
        for (UpdateInfo info : updates) {
            updateMap.put(info.appId(), info);
        }
    }

    @Override
    public DomainResult<List<UpdateInfo>> checkUpdates() {
        return new DomainResult.Success<>(new ArrayList<>(updateMap.values()));
    }

    @Override
    public DomainResult<InstallTask> updateApp(AppId appId) {
        if (!updateMap.containsKey(appId)) {
            return new DomainResult.Failure<>(new DomainError("UPDATE_NOT_FOUND", "no update found for app", null));
        }
        InstallTask task = new InstallTask(
                "task-" + appId.value(),
                appId,
                InstallTaskStatus.SUCCEEDED,
                100,
                null
        );
        return new DomainResult.Success<>(task);
    }

    @Override
    public DomainResult<List<InstallTask>> batchUpdate(List<AppId> appIds) {
        List<InstallTask> tasks = appIds.stream()
                .map(this::updateApp)
                .filter(result -> result instanceof DomainResult.Success<InstallTask>)
                .map(result -> ((DomainResult.Success<InstallTask>) result).value())
                .collect(Collectors.toList());
        return new DomainResult.Success<>(tasks);
    }
}
