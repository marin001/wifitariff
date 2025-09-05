package com.m3connect.wifiapi.model;

import java.util.List;
import java.util.Map;

/** Represents a WiFi tariff with id, name, features, and prices. */
public class Tariff {
  private Long id;
  private String name;
  private List<String> features;
  private Map<String, Double> prices;
  private Long remoteId; // Optional remote ID

  /** Default constructor for Tariff. */
  public Tariff() {}

  /**
   * Constructs a Tariff with all fields. Defensive copies are made for mutable fields.
   *
   * @param id the tariff id
   * @param name the tariff name
   * @param features the list of features
   * @param prices the map of prices
   */
  public Tariff(Long id, String name, List<String> features, Map<String, Double> prices) {
    this(id, name, features, prices, null);
  }

  /**
   * Overloaded constructor to support optional remoteId.
   */
  public Tariff(Long id, String name, List<String> features, Map<String, Double> prices, Long remoteId) {
    this.id = id;
    this.name = name;
    this.features = features == null ? null : new java.util.ArrayList<>(features);
    this.prices = prices == null ? null : new java.util.HashMap<>(prices);
    this.remoteId = remoteId;
  }

  /** Gets the remoteId (optional). */
  public Long getRemoteId() {
    return remoteId;
  }

  /** Sets the remoteId (optional). */
  public void setRemoteId(Long remoteId) {
    this.remoteId = remoteId;
  }


  /** Gets the tariff id. */
  public Long getId() {
    return id;
  }

  /** Sets the tariff id. */
  public void setId(Long id) {
    this.id = id;
  }

  /** Gets the tariff name. */
  public String getName() {
    return name;
  }

  /** Sets the tariff name. */
  public void setName(String name) {
    this.name = name;
  }

  /** Gets a copy of the features list to avoid exposing internal state. */
  public List<String> getFeatures() {
    return features == null ? null : new java.util.ArrayList<>(features);
  }

  /** Sets the features list, making a defensive copy. */
  public void setFeatures(List<String> features) {
    this.features = features == null ? null : new java.util.ArrayList<>(features);
  }

  /** Gets a copy of the prices map to avoid exposing internal state. */
  public Map<String, Double> getPrices() {
    return prices == null ? null : new java.util.HashMap<>(prices);
  }

  /** Sets the prices map, making a defensive copy. */
  public void setPrices(Map<String, Double> prices) {
    this.prices = prices == null ? null : new java.util.HashMap<>(prices);
  }
}
