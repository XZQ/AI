package com.car.appstore.feature.settings.domain;

import com.car.appstore.core.domain.DomainResult;
import com.car.appstore.core.domain.UserSetting;

public interface SettingsRepository {
    DomainResult<UserSetting> fetchSettings();

    DomainResult<Void> updateSettings(UserSetting newSetting);
}
