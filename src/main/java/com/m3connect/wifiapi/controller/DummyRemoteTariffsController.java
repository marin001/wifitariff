package com.m3connect.wifiapi.controller;

import com.m3connect.wifiapi.model.Tariff;
import java.time.Instant;
import java.util.*;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DummyRemoteTariffsController {
    private static final int TARIFF_COUNT = 20;
    private static final int ID_MIN = 5000;
    private static final int ID_MAX = 5050;
    private static final double PRICE_MIN = 10.0;
    private static final double PRICE_MAX = 100.0;

    @GetMapping(value = "/dummy-remote-tariffs", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Tariff> getDummyRemoteTariffs() {
        Random random = new Random();
        String timestamp = Instant.now().toString();
        Set<Long> usedIds = new HashSet<>();

        return generateTariffs(random, timestamp, usedIds);
    }

    private List<Tariff> generateTariffs(Random random, String timestamp, Set<Long> usedIds) {
        return java.util.stream.IntStream.range(0, TARIFF_COUNT)
            .mapToObj(i -> createRandomTariff(i, random, timestamp, usedIds))
            .toList();
    }

    private Tariff createRandomTariff(int index, Random random, String timestamp, Set<Long> usedIds) {
        long id;
        do {
            id = ID_MIN + random.nextInt(ID_MAX - ID_MIN + 1);
        } while (!usedIds.add(id)); // ensure unique ID

        double price = PRICE_MIN + (PRICE_MAX - PRICE_MIN) * random.nextDouble();
        Map<String, Double> prices = new HashMap<>();
        prices.put("monthly", price);

        String name = "Remote Tariff " + " " + timestamp;
        List<String> features = Arrays.asList(
            index % 2 == 0 ? "Fast" : "Faster",
            index % 3 == 0 ? "Secure" : "More Secure"
        );

                return new Tariff(id, name, features, prices, null);
    }
}
