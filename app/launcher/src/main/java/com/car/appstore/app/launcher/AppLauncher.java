package com.car.appstore.app.launcher;

import com.car.appstore.core.domain.AppId;
import com.car.appstore.core.domain.AppInfo;
import com.car.appstore.core.domain.DomainResult;
import com.car.appstore.core.domain.InstallTask;
import com.car.appstore.core.domain.NetworkPolicy;
import com.car.appstore.core.domain.UpdateInfo;
import com.car.appstore.core.domain.UserSetting;
import com.car.appstore.core.domain.Version;
import com.car.appstore.core.updateengine.UpdateEvent;
import com.car.appstore.core.updateengine.UpdateState;
import com.car.appstore.core.updateengine.UpdateStateMachine;
import com.car.appstore.feature.home.data.InMemoryHomeRepository;
import com.car.appstore.feature.search.data.InMemorySearchRepository;
import com.car.appstore.feature.settings.data.InMemorySettingsRepository;
import com.car.appstore.feature.update.data.InMemoryUpdateRepository;

import java.util.List;

public class AppLauncher {

    public static void main(String[] args) {
        AppInfo nav = new AppInfo(new AppId("nav"), "Navigation", "icon://nav", 4.8, new Version("1.0.0", 1));
        AppInfo music = new AppInfo(new AppId("music"), "Music", "icon://music", 4.6, new Version("2.3.0", 23));

        InMemoryHomeRepository homeRepository = new InMemoryHomeRepository(List.of(nav, music));
        InMemorySearchRepository searchRepository = new InMemorySearchRepository(List.of(nav, music), List.of("导航", "音乐"));
        InMemorySettingsRepository settingsRepository = new InMemorySettingsRepository(
                new UserSetting(true, true, NetworkPolicy.WIFI_ONLY)
        );
        InMemoryUpdateRepository updateRepository = new InMemoryUpdateRepository(List.of(
                new UpdateInfo(nav.appId(), nav.version(), new Version("1.1.0", 2), false)
        ));

        printHome(homeRepository.loadHomeFeeds());
        printSearch(searchRepository.search("nav"));
        printSettings(settingsRepository.fetchSettings());
        printUpdate(updateRepository.updateApp(nav.appId()));
        printStateMachineDemo();
    }

    private static void printHome(DomainResult<List<AppInfo>> result) {
        if (result instanceof DomainResult.Success<List<AppInfo>> success) {
            System.out.println("[HOME] apps=" + success.value().size());
        }
    }

    private static void printSearch(DomainResult<List<AppInfo>> result) {
        if (result instanceof DomainResult.Success<List<AppInfo>> success) {
            System.out.println("[SEARCH] matched=" + success.value().size());
        }
    }

    private static void printSettings(DomainResult<UserSetting> result) {
        if (result instanceof DomainResult.Success<UserSetting> success) {
            System.out.println("[SETTINGS] networkPolicy=" + success.value().networkPolicy());
        }
    }

    private static void printUpdate(DomainResult<InstallTask> result) {
        if (result instanceof DomainResult.Success<InstallTask> success) {
            System.out.println("[UPDATE] status=" + success.value().status());
        }
    }

    private static void printStateMachineDemo() {
        UpdateStateMachine machine = new UpdateStateMachine();
        UpdateState state = UpdateState.IDLE;
        state = machine.transition(state, UpdateEvent.ENQUEUE).to();
        state = machine.transition(state, UpdateEvent.START).to();
        state = machine.transition(state, UpdateEvent.DOWNLOAD_COMPLETE).to();
        state = machine.transition(state, UpdateEvent.VERIFY_PASS).to();
        state = machine.transition(state, UpdateEvent.INSTALL_SUCCESS).to();
        System.out.println("[ENGINE] finalState=" + state);
    }
}
