package com.m3connect.wifiapi.controller;

import com.m3connect.wifiapi.model.Tariff;
import com.m3connect.wifiapi.store.TariffStore;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DummyListAllTariffsController {
    private final TariffStore tariffStore;

    public DummyListAllTariffsController(TariffStore tariffStore) {
        this.tariffStore = tariffStore;
    }

    @GetMapping("/dummy-list-all-tariffs")
    public List<Tariff> listAllTariffs() {
        return new java.util.ArrayList<>(tariffStore.getTariffMap().values());
    }
}
