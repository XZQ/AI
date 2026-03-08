package com.car.appstore.feature.settings.domain;

import com.car.appstore.core.domain.DomainResult;
import com.car.appstore.core.domain.UserSetting;

@FunctionalInterface
public interface UpdateUserSettingUseCase {
    DomainResult<Void> execute(UserSetting newSetting);
}
