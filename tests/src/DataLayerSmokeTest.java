import com.car.appstore.core.domain.*;
import com.car.appstore.feature.home.data.InMemoryHomeRepository;
import com.car.appstore.feature.search.data.InMemorySearchRepository;
import com.car.appstore.feature.settings.data.InMemorySettingsRepository;
import com.car.appstore.feature.update.data.InMemoryUpdateRepository;
import com.car.appstore.feature.update.domain.BatchUpdateReport;

import java.util.List;

public class DataLayerSmokeTest {
    public static void main(String[] args) {
        AppInfo app = new AppInfo(new AppId("nav"), "Navigation", "icon", 4.8, new Version("1.0", 1));
        InMemoryHomeRepository home = new InMemoryHomeRepository(List.of(app));
        DomainResult<List<AppInfo>> homeResult = home.loadHomeFeeds();
        require(homeResult instanceof DomainResult.Success<List<AppInfo>>, "home result type");

        InMemorySearchRepository search = new InMemorySearchRepository(List.of(app), List.of("导航"));
        DomainResult<List<AppInfo>> searchResult = search.search("nav");
        require(searchResult instanceof DomainResult.Success<List<AppInfo>>, "search result type");

        InMemorySettingsRepository settings = new InMemorySettingsRepository(new UserSetting(true, true, NetworkPolicy.WIFI_ONLY));
        settings.updateSettings(new UserSetting(false, true, NetworkPolicy.ANY));
        DomainResult<UserSetting> settingResult = settings.fetchSettings();
        require(settingResult instanceof DomainResult.Success<UserSetting>, "settings result type");

        UpdateInfo updateInfo = new UpdateInfo(new AppId("nav"), new Version("1.0", 1), new Version("1.1", 2), false);
        InMemoryUpdateRepository update = new InMemoryUpdateRepository(List.of(updateInfo));
        DomainResult<InstallTask> taskResult = update.updateApp(new AppId("nav"));
        require(taskResult instanceof DomainResult.Success<InstallTask>, "update result type");

        DomainResult<BatchUpdateReport> batchResult = update.batchUpdate(List.of(new AppId("nav"), new AppId("music")));
        require(batchResult instanceof DomainResult.Success<BatchUpdateReport>, "batch update result type");
        BatchUpdateReport report = ((DomainResult.Success<BatchUpdateReport>) batchResult).value();
        require(report.succeededTasks().size() == 1, "batch update success count");
        require(report.failedByAppId().containsKey(new AppId("music")), "batch update failure detail");

        System.out.println("DataLayerSmokeTest: OK");
    }

    private static void require(boolean cond, String msg) {
        if (!cond) throw new IllegalStateException(msg);
    }
}
