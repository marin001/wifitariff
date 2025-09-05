package com.m3connect.wifiapi.service;

import com.m3connect.wifiapi.model.Tariff;
import com.m3connect.wifiapi.store.TariffStore;
import java.util.List;
import java.util.Map;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TariffSyncService {
    private final RestTemplate restTemplate = new RestTemplate();
    private static final String REMOTE_TARIFFS_URL = "http://localhost:8080/dummy-remote-tariffs";

    private final TariffStore tariffStore;

    public TariffSyncService(TariffStore tariffStore) {
        this.tariffStore = tariffStore;
    }

    @Scheduled(fixedRate = 60000) // Runs every 60 seconds
    public void syncTariffs() {
        List<Tariff> remoteTariffs = fetchRemoteTariffs();
        mergeTariffs(remoteTariffs);
    }

    public List<Tariff> fetchRemoteTariffs() {
        // Replace Tariff[].class with your actual model
        Tariff[] tariffs = restTemplate.getForObject(REMOTE_TARIFFS_URL, Tariff[].class);
        return tariffs != null ? java.util.Arrays.asList(tariffs) : java.util.Collections.emptyList();
    }

   private void mergeTariffs(List<Tariff> remoteTariffs) {
    Map<Long, Tariff> localMap = tariffStore.getTariffMap();

    for (Tariff remote : remoteTariffs) {
        // Find existing tariff by matching remote ID with local tariffs' remoteId
        Tariff existing = localMap.values().stream()
            .filter(local -> remote.getId().equals(local.getRemoteId()))
            .findFirst()
            .orElse(null);

        if (existing != null) {
            // Update existing tariff via store method
            tariffStore.updateTariff(existing.getId(), remote);
        } else {
            // Add new tariff with new local ID and remoteId set
            long newLocalId = tariffStore.getNextId();
            Tariff newTariff = new Tariff(
                newLocalId,
                remote.getName(),
                remote.getFeatures(),
                remote.getPrices(),
                remote.getId()
            );
            tariffStore.addTariff(newTariff);
        }
    }
    }


    public List<Tariff> getLocalTariffs() {
        return new java.util.ArrayList<>(tariffStore.getTariffMap().values());
    }
}
