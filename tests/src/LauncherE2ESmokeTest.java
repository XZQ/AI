import com.car.appstore.app.launcher.AppFlowOrchestrator;
import com.car.appstore.core.domain.AppDetail;
import com.car.appstore.core.domain.AppId;
import com.car.appstore.core.domain.AppInfo;
import com.car.appstore.core.domain.ErrorCodes;
import com.car.appstore.core.domain.NetworkPolicy;
import com.car.appstore.core.domain.UpdateInfo;
import com.car.appstore.core.domain.UserSetting;
import com.car.appstore.core.domain.Version;
import com.car.appstore.feature.detail.data.InMemoryDetailRepository;
import com.car.appstore.feature.home.data.InMemoryHomeRepository;
import com.car.appstore.feature.settings.data.InMemorySettingsRepository;
import com.car.appstore.feature.update.data.InMemoryUpdateRepository;

import java.util.List;
import java.util.Map;

public class LauncherE2ESmokeTest {
    public static void main(String[] args) {
        AppInfo nav = new AppInfo(new AppId("nav"), "Navigation", "icon", 4.8, new Version("1.0", 1));
        AppInfo music = new AppInfo(new AppId("music"), "Music", "icon", 4.2, new Version("1.0", 1));

        AppFlowOrchestrator orchestrator = new AppFlowOrchestrator(
                new InMemoryHomeRepository(List.of(nav, music)),
                new InMemoryDetailRepository(Map.of(nav.appId(), new AppDetail(nav, "desc", List.of(), List.of(), "log"))),
                new InMemorySettingsRepository(new UserSetting(true, true, NetworkPolicy.WIFI_ONLY)),
                new InMemoryUpdateRepository(List.of(new UpdateInfo(nav.appId(), nav.version(), new Version("1.1", 2), false)))
        );

        AppFlowOrchestrator.FlowReport success = orchestrator.runMainFlow(nav.appId());
        require(success.homeLoaded() && success.detailLoaded() && success.settingsLoaded() && success.updateSucceeded(), "success path failed");

        AppFlowOrchestrator.FlowReport failure = orchestrator.runMainFlow(music.appId());
        require(!failure.updateSucceeded(), "failure path should not succeed");
        require(ErrorCodes.DETAIL_NOT_FOUND.equals(failure.errorCode()) || ErrorCodes.UPDATE_NOT_FOUND.equals(failure.errorCode()), "unexpected error code");

        System.out.println("LauncherE2ESmokeTest: OK");
    }

    private static void require(boolean cond, String msg) {
        if (!cond) throw new IllegalStateException(msg);
    }
}
