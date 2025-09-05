package com.m3connect.wifiapi;

import com.m3connect.wifiapi.model.Tariff;
import com.m3connect.wifiapi.service.TariffSyncService;
import com.m3connect.wifiapi.store.TariffStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

@SpringBootTest
@ActiveProfiles("test")
class TariffSyncServiceTests {

    @Autowired
    private TariffStore tariffStore;

    @SpyBean
    private TariffSyncService tariffSyncService;

    @BeforeEach
    void setup() {
        tariffStore.clear();
    }
@Test
void testSyncAddsNewTariffs() {
    Tariff remoteTariff = new Tariff(
        50L,
        "Mock Plan",
        List.of("Fast Internet"),
        Map.of("monthly", 49.99),
        100L
    );

    doReturn(List.of(remoteTariff))
        .when(tariffSyncService)
        .fetchRemoteTariffs();

    tariffSyncService.syncTariffs();

    List<Tariff> localTariffs = tariffStore.getTariffMap().values().stream().toList();
    assertThat(localTariffs).hasSize(1); // 💡 Add this line to guard against null
    Tariff local = localTariffs.get(0);
    assertThat(local).isNotNull(); // 💡 This will help isolate the issue
    assertThat(local.getRemoteId()).isEqualTo(50L);
}


    @Test
    void testSyncUpdatesExistingTariff() {
        Tariff existing = new Tariff(
            1L,
            "Old Plan",
            List.of("Old Feature"),
            Map.of("monthly", 10.0),
            100L
        );
        tariffStore.getTariffMap().put(1L, existing);

        Tariff remoteTariff = new Tariff(
            100L,
            "Updated Plan",
            List.of("Updated Feature"),
            Map.of("monthly", 99.0),
            null
        );

        doReturn(List.of(remoteTariff))
            .when(tariffSyncService)
            .fetchRemoteTariffs();

        tariffSyncService.syncTariffs();

        Tariff updated = tariffStore.getTariffMap().get(1L);
        assertThat(updated.getName()).isEqualTo("Updated Plan");
        assertThat(updated.getFeatures()).containsExactly("Updated Feature");
        assertThat(updated.getPrices().get("monthly")).isEqualTo(99.0);
    }
}
