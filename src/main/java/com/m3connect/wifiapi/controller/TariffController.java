/*
 * TariffController.java
 *
 * REST controller for managing WiFi tariffs in the wifiapi project.
 * Provides CRUD endpoints for Tariff entities using an in-memory store.
 */

package com.m3connect.wifiapi.controller;

import com.m3connect.wifiapi.model.Tariff;
import jakarta.validation.Valid;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for WiFi tariffs. Handles create, read, update, and delete operations for Tariff
 * entities. Uses an in-memory store for demonstration purposes.
 */
@RestController
@RequestMapping("/tariffs")
@Validated
public class TariffController {
  /** In-memory store for tariffs. */
  private final Map<Long, Tariff> tariffStore = new HashMap<>();

  /** ID generator for tariffs. */
  private final AtomicLong idGenerator = new AtomicLong(1);

  /**
   * Creates a new tariff.
   *
   * @param tariff the tariff to create
   * @return the created tariff with generated ID
   */
  @PostMapping
  public ResponseEntity<Tariff> createTariff(@Valid @RequestBody Tariff tariff) {
    long id = idGenerator.getAndIncrement();
    tariff.setId(id);
    tariffStore.put(id, tariff);
    return ResponseEntity.ok(tariff);
  }

  /**
   * Retrieves a tariff by ID.
   *
   * @param id the tariff ID
   * @return the tariff if found, or 404 if not found
   */
  @GetMapping("/{id}")
  public ResponseEntity<Tariff> getTariff(@PathVariable Long id) {
    Tariff tariff = tariffStore.get(id);
    if (tariff == null) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(tariff);
  }

  /**
   * Updates an existing tariff by ID.
   *
   * @param id the tariff ID
   * @param tariff the updated tariff data
   * @return the updated tariff, or 404 if not found
   */
  @PutMapping("/{id}")
  public ResponseEntity<Tariff> updateTariff(
      @PathVariable Long id, @Valid @RequestBody Tariff tariff) {
    if (!tariffStore.containsKey(id)) {
      return ResponseEntity.notFound().build();
    }
    tariff.setId(id);
    tariffStore.put(id, tariff);
    return ResponseEntity.ok(tariff);
  }

  /**
   * Deletes a tariff by ID.
   *
   * @param id the tariff ID
   * @return the deleted tariff, or 404 if not found
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<Tariff> deleteTariff(@PathVariable Long id) {
    Tariff removed = tariffStore.remove(id);
    if (removed == null) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(removed);
  }
}
