package com.car.appstore.app.launcher;

import com.car.appstore.core.domain.AppDetail;
import com.car.appstore.core.domain.AppId;
import com.car.appstore.core.domain.AppInfo;
import com.car.appstore.core.domain.DomainResult;
import com.car.appstore.core.domain.InstallTask;
import com.car.appstore.core.domain.UserSetting;
import com.car.appstore.feature.detail.domain.DetailRepository;
import com.car.appstore.feature.home.domain.HomeRepository;
import com.car.appstore.feature.settings.domain.SettingsRepository;
import com.car.appstore.feature.update.domain.UpdateRepository;

import java.util.List;

public class AppFlowOrchestrator {
    private final HomeRepository homeRepository;
    private final DetailRepository detailRepository;
    private final SettingsRepository settingsRepository;
    private final UpdateRepository updateRepository;

    public AppFlowOrchestrator(HomeRepository homeRepository,
                               DetailRepository detailRepository,
                               SettingsRepository settingsRepository,
                               UpdateRepository updateRepository) {
        this.homeRepository = homeRepository;
        this.detailRepository = detailRepository;
        this.settingsRepository = settingsRepository;
        this.updateRepository = updateRepository;
    }

    public FlowReport runMainFlow(AppId appId) {
        DomainResult<List<AppInfo>> homeResult = homeRepository.loadHomeFeeds();
        DomainResult<AppDetail> detailResult = detailRepository.getAppDetail(appId);
        DomainResult<UserSetting> settingsResult = settingsRepository.fetchSettings();
        DomainResult<InstallTask> updateResult = updateRepository.updateApp(appId);

        String errorCode = extractErrorCode(detailResult);
        if (errorCode == null) {
            errorCode = extractErrorCode(updateResult);
        }

        return new FlowReport(
                homeResult instanceof DomainResult.Success<List<AppInfo>>,
                detailResult instanceof DomainResult.Success<AppDetail>,
                settingsResult instanceof DomainResult.Success<UserSetting>,
                updateResult instanceof DomainResult.Success<InstallTask>,
                errorCode
        );
    }

    private static <T> String extractErrorCode(DomainResult<T> result) {
        if (result instanceof DomainResult.Failure<T> failure) {
            return failure.error().code();
        }
        return null;
    }

    public record FlowReport(
            boolean homeLoaded,
            boolean detailLoaded,
            boolean settingsLoaded,
            boolean updateSucceeded,
            String errorCode
    ) {}
}
