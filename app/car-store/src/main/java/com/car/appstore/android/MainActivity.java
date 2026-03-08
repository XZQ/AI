package com.car.appstore.android;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.car.appstore.core.domain.AppDetail;
import com.car.appstore.core.domain.AppId;
import com.car.appstore.core.domain.AppInfo;
import com.car.appstore.core.domain.DomainResult;
import com.car.appstore.core.domain.NetworkPolicy;
import com.car.appstore.core.domain.UpdateInfo;
import com.car.appstore.core.domain.UserSetting;
import com.car.appstore.core.domain.Version;
import com.car.appstore.feature.detail.data.InMemoryDetailRepository;
import com.car.appstore.feature.home.data.InMemoryHomeRepository;
import com.car.appstore.feature.search.data.InMemorySearchRepository;
import com.car.appstore.feature.settings.data.InMemorySettingsRepository;
import com.car.appstore.feature.update.data.InMemoryUpdateRepository;
import com.car.appstore.feature.update.domain.BatchUpdateReport;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView summaryView = findViewById(R.id.summary_text);
        summaryView.setText(buildDemoSummary());
    }

    private String buildDemoSummary() {
        AppInfo nav = new AppInfo(new AppId("nav"), "Navigation", "icon://nav", 4.8, new Version("1.0.0", 1));
        AppInfo music = new AppInfo(new AppId("music"), "Music", "icon://music", 4.6, new Version("2.3.0", 23));

        InMemoryHomeRepository homeRepository = new InMemoryHomeRepository(Arrays.asList(nav, music));
        InMemorySearchRepository searchRepository = new InMemorySearchRepository(Arrays.asList(nav, music), Arrays.asList("导航", "音乐"));
        Map<AppId, AppDetail> detailMap = new HashMap<>();
        detailMap.put(nav.appId(), new AppDetail(nav, "Best navigation app", Arrays.asList("s1", "s2"), Arrays.asList("location"), "fix bugs"));
        InMemoryDetailRepository detailRepository = new InMemoryDetailRepository(detailMap);
        InMemorySettingsRepository settingsRepository = new InMemorySettingsRepository(
                new UserSetting(true, true, NetworkPolicy.WIFI_ONLY)
        );
        InMemoryUpdateRepository updateRepository = new InMemoryUpdateRepository(Arrays.asList(
                new UpdateInfo(nav.appId(), nav.version(), new Version("1.1.0", 2), false)
        ));

        StringBuilder sb = new StringBuilder();
        sb.append("Car AppStore Demo\n\n");

        DomainResult<List<AppInfo>> home = homeRepository.loadHomeFeeds();
        sb.append("Home loaded: ").append(home instanceof DomainResult.Success).append('\n');

        DomainResult<List<AppInfo>> search = searchRepository.search("nav");
        int searchCount = 0;
        if (search instanceof DomainResult.Success) {
            DomainResult.Success<List<AppInfo>> success = (DomainResult.Success<List<AppInfo>>) search;
            searchCount = success.value().size();
        }
        sb.append("Search \"nav\" result count: ").append(searchCount).append('\n');

        DomainResult<AppDetail> detail = detailRepository.getAppDetail(nav.appId());
        sb.append("Detail loaded: ").append(detail instanceof DomainResult.Success).append('\n');

        DomainResult<UserSetting> settings = settingsRepository.fetchSettings();
        String networkPolicy = "UNKNOWN";
        if (settings instanceof DomainResult.Success) {
            DomainResult.Success<UserSetting> success = (DomainResult.Success<UserSetting>) settings;
            networkPolicy = success.value().networkPolicy().name();
        }
        sb.append("Network policy: ").append(networkPolicy).append('\n');

        DomainResult<BatchUpdateReport> batch = updateRepository.batchUpdate(Arrays.asList(nav.appId(), music.appId()));
        int successCount = 0;
        int failCount = 0;
        if (batch instanceof DomainResult.Success) {
            DomainResult.Success<BatchUpdateReport> success = (DomainResult.Success<BatchUpdateReport>) batch;
            successCount = success.value().succeededTasks().size();
            failCount = success.value().failedByAppId().size();
        }
        sb.append(String.format(Locale.ROOT, "Batch update: success=%d, failed=%d", successCount, failCount));

        return sb.toString();
    }
}
