package com.car.appstore.app.launcher;

import com.car.appstore.core.domain.AppDetail;
import com.car.appstore.core.domain.AppId;
import com.car.appstore.core.domain.AppInfo;
import com.car.appstore.core.domain.NetworkPolicy;
import com.car.appstore.core.domain.UpdateInfo;
import com.car.appstore.core.domain.UserSetting;
import com.car.appstore.core.domain.Version;
import com.car.appstore.core.updateengine.UpdateEvent;
import com.car.appstore.core.updateengine.UpdateState;
import com.car.appstore.core.updateengine.UpdateStateMachine;
import com.car.appstore.feature.detail.data.InMemoryDetailRepository;
import com.car.appstore.feature.home.data.InMemoryHomeRepository;
import com.car.appstore.feature.settings.data.InMemorySettingsRepository;
import com.car.appstore.feature.update.data.InMemoryUpdateRepository;

import java.util.List;
import java.util.Map;

public class AppLauncher {

    public static void main(String[] args) {
        AppInfo nav = new AppInfo(new AppId("nav"), "Navigation", "icon://nav", 4.8, new Version("1.0.0", 1));
        AppInfo music = new AppInfo(new AppId("music"), "Music", "icon://music", 4.6, new Version("2.3.0", 23));

        InMemoryHomeRepository homeRepository = new InMemoryHomeRepository(List.of(nav, music));
        InMemoryDetailRepository detailRepository = new InMemoryDetailRepository(Map.of(
                nav.appId(), new AppDetail(nav, "Best navigation app", List.of("s1", "s2"), List.of("location"), "fix bugs")
        ));
        InMemorySettingsRepository settingsRepository = new InMemorySettingsRepository(
                new UserSetting(true, true, NetworkPolicy.WIFI_ONLY)
        );
        InMemoryUpdateRepository updateRepository = new InMemoryUpdateRepository(List.of(
                new UpdateInfo(nav.appId(), nav.version(), new Version("1.1.0", 2), false)
        ));

        AppFlowOrchestrator orchestrator = new AppFlowOrchestrator(
                homeRepository,
                detailRepository,
                settingsRepository,
                updateRepository
        );

        AppFlowOrchestrator.FlowReport success = orchestrator.runMainFlow(nav.appId());
        System.out.println("[FLOW] home=" + success.homeLoaded()
                + " detail=" + success.detailLoaded()
                + " settings=" + success.settingsLoaded()
                + " update=" + success.updateSucceeded());

        AppFlowOrchestrator.FlowReport failure = orchestrator.runMainFlow(music.appId());
        if (failure.errorCode() != null) {
            System.out.println("[FLOW-ERROR] code=" + failure.errorCode());
        }

        printStateMachineDemo();
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
