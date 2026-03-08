package com.car.appstore.feature.update.domain;

import com.car.appstore.core.domain.AppId;
import com.car.appstore.core.domain.DomainResult;
import com.car.appstore.core.domain.InstallTask;
import com.car.appstore.core.domain.UpdateInfo;

import java.util.List;

public interface UpdateRepository {
    DomainResult<List<UpdateInfo>> checkUpdates();

    DomainResult<InstallTask> updateApp(AppId appId);

    DomainResult<BatchUpdateReport> batchUpdate(List<AppId> appIds);
}
