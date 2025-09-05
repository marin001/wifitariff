package com.m3connect.wifiapi.store;

import com.m3connect.wifiapi.model.Tariff;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Shared in-memory store for tariffs.
 */
@Component
public class TariffStore {
    private final Map<Long, Tariff> tariffMap = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public Map<Long, Tariff> getTariffMap() {
        return tariffMap;
    }

    public long getNextId() {
        return idGenerator.getAndIncrement();
    }

    // Update fields of an existing Tariff by id
    public void updateTariff(Long localId, Tariff newTariffData) {
        Tariff existing = tariffMap.get(localId);
        if (existing != null) {
            existing.setName(newTariffData.getName());
            existing.setFeatures(newTariffData.getFeatures());
            existing.setPrices(newTariffData.getPrices());
        }
    }

    public void clear() {
        tariffMap.clear();
        idGenerator.set(1);
    }

    public void addTariff(Tariff tariff) {
        tariffMap.put(tariff.getId(), tariff);
    }
}
