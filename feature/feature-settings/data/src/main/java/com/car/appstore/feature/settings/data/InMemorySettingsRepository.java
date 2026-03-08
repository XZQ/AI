package com.car.appstore.feature.settings.data;

import com.car.appstore.core.domain.DomainResult;
import com.car.appstore.core.domain.UserSetting;
import com.car.appstore.feature.settings.domain.SettingsRepository;

import java.util.concurrent.atomic.AtomicReference;

public class InMemorySettingsRepository implements SettingsRepository {
    private final AtomicReference<UserSetting> settingRef;

    public InMemorySettingsRepository(UserSetting initialSetting) {
        this.settingRef = new AtomicReference<>(initialSetting);
    }

    @Override
    public DomainResult<UserSetting> fetchSettings() {
        return new DomainResult.Success<>(settingRef.get());
    }

    @Override
    public DomainResult<Void> updateSettings(UserSetting newSetting) {
        settingRef.set(newSetting);
        return new DomainResult.Success<>(null);
    }
}
