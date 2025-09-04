/*
 * TariffControllerTest.java
 *
 * Integration tests for TariffController CRUD and edge cases.
 * Uses MockMvc to simulate HTTP requests and validate responses.
 */

package com.m3connect.wifiapi.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.m3connect.wifiapi.model.Tariff;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Integration tests for TariffController. Covers basic CRUD and edge/error cases for WiFi tariffs.
 */
@SpringBootTest
@AutoConfigureMockMvc
class TariffControllerTest {
  private static final String API_KEY_HEADER = "X-API-KEY";

  @org.springframework.beans.factory.annotation.Value("${api.key}")
  private String apiKeyValue;

  /** MockMvc for simulating HTTP requests. */
  @Autowired private MockMvc mockMvc;

  /** ObjectMapper for JSON serialization/deserialization. */
  @Autowired private ObjectMapper objectMapper;

  @Test
  void testCreateTariff() throws Exception {
    // Should create a new tariff and return it with an ID
    Map<String, Double> prices = new HashMap<>();
    prices.put("monthly", 19.99);
    Tariff tariff = new Tariff(null, "Basic Tariff", Arrays.asList("Fast", "Secure"), prices);
    mockMvc
        .perform(
            post("/tariffs")
                .header(API_KEY_HEADER, apiKeyValue)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tariff)))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").exists())
        .andExpect(jsonPath("$.name").value("Basic Tariff"));
  }

  @Test
  void testGetTariff() throws Exception {
    // Should retrieve a tariff by its ID after creation
    // First create a tariff
    Map<String, Double> prices = new HashMap<>();
    prices.put("monthly", 19.99);
    Tariff tariff = new Tariff(null, "Basic Tariff", Arrays.asList("Fast", "Secure"), prices);
    String response =
        mockMvc
            .perform(
                post("/tariffs")
                    .header(API_KEY_HEADER, apiKeyValue)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(tariff)))
            .andReturn()
            .getResponse()
            .getContentAsString();
    Tariff created = objectMapper.readValue(response, Tariff.class);

    // Now get the tariff
    mockMvc
        .perform(get("/tariffs/" + created.getId()).header(API_KEY_HEADER, apiKeyValue))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(created.getId()));
  }

  @Test
  void testUpdateTariff() throws Exception {
    // Should update an existing tariff's name and return the updated tariff
    // Create
    Map<String, Double> prices = new HashMap<>();
    prices.put("monthly", 19.99);
    Tariff tariff = new Tariff(null, "Basic Tariff", Arrays.asList("Fast", "Secure"), prices);
    String response =
        mockMvc
            .perform(
                post("/tariffs")
                    .header(API_KEY_HEADER, apiKeyValue)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(tariff)))
            .andReturn()
            .getResponse()
            .getContentAsString();
    Tariff created = objectMapper.readValue(response, Tariff.class);

    // Update
    created.setName("Updated Tariff");
    mockMvc
        .perform(
            put("/tariffs/" + created.getId())
                .header(API_KEY_HEADER, apiKeyValue)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(created)))
        // .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("Updated Tariff"));
  }

  @Test
  void testDeleteTariff() throws Exception {
    // Should delete an existing tariff and return the deleted tariff
    // Create
    Map<String, Double> prices = new HashMap<>();
    prices.put("monthly", 19.99);
    Tariff tariff = new Tariff(null, "Basic Tariff", Arrays.asList("Fast", "Secure"), prices);
    String response =
        mockMvc
            .perform(
                post("/tariffs")
                    .header(API_KEY_HEADER, apiKeyValue)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(tariff)))
            .andReturn()
            .getResponse()
            .getContentAsString();
    Tariff created = objectMapper.readValue(response, Tariff.class);

    // Delete
    mockMvc
        .perform(delete("/tariffs/" + created.getId()).header(API_KEY_HEADER, apiKeyValue))
        // .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(created.getId()));
  }

  @Test
  void testCreateTariffWithMissingFields() throws Exception {
    // Should allow creation with missing fields (no validation, returns 200)
    // Missing name and prices
    String invalidTariffJson = "{\"features\":[\"Fast\"]}";
    mockMvc
        .perform(
            post("/tariffs")
                .header(API_KEY_HEADER, apiKeyValue)
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidTariffJson))
        // .andDo(print())
        .andExpect(status().isOk()); // Controller does not validate, so still 200
  }

  @Test
  void testCreateTariffWithInvalidDataType() throws Exception {
    // Should return 400 Bad Request for invalid data type in prices
    // prices as string instead of map
    String invalidTariffJson =
        "{\"name\":\"Invalid\",\"features\":[\"Fast\"],\"prices\":\"notAMap\"}";
    mockMvc
        .perform(
            post("/tariffs")
                .header(API_KEY_HEADER, apiKeyValue)
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidTariffJson))
        // .andDo(print())
        .andExpect(status().isBadRequest());
  }

  @Test
  void testCreateTariffWithNullBody() throws Exception {
    // Should return 400 Bad Request for null request body
    mockMvc
        .perform(
            post("/tariffs")
                .header(API_KEY_HEADER, apiKeyValue)
                .contentType(MediaType.APPLICATION_JSON))
        // .andDo(print())
        .andExpect(status().isBadRequest());
  }

  @Test
  void testGetTariffWithNonExistentId() throws Exception {
    // Should return 404 Not Found for non-existent tariff ID
    mockMvc
        .perform(get("/tariffs/99999").header(API_KEY_HEADER, apiKeyValue)) // .andDo(print())
        .andExpect(status().isNotFound());
  }

  @Test
  void testGetTariffWithInvalidIdFormat() throws Exception {
    // Should return 400 Bad Request for invalid ID format
    mockMvc
        .perform(get("/tariffs/invalid").header(API_KEY_HEADER, apiKeyValue)) // .andDo(print())
        .andExpect(status().isBadRequest());
  }

  @Test
  void testUpdateTariffWithNonExistentId() throws Exception {
    // Should return 404 Not Found when updating a non-existent tariff
    Map<String, Double> prices = new HashMap<>();
    prices.put("monthly", 19.99);
    Tariff tariff = new Tariff(null, "NonExistent", Arrays.asList("Basic"), prices);
    mockMvc
        .perform(
            put("/tariffs/99999")
                .header(API_KEY_HEADER, apiKeyValue)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tariff)))
        // .andDo(print())
        .andExpect(status().isNotFound());
  }

  @Test
  void testUpdateTariffWithMissingFields() throws Exception {
    // Should allow update with missing fields (no validation, returns 200)
    // Missing name and prices
    String invalidTariffJson = "{\"features\":[\"Fast\"]}";
    mockMvc
        .perform(
            put("/tariffs/1")
                .header(API_KEY_HEADER, apiKeyValue)
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidTariffJson))
        // .andDo(print())
        .andExpect(status().isOk()); // Controller does not validate, so still 200
  }

  @Test
  void testUpdateTariffWithInvalidIdFormat() throws Exception {
    // Should return 400 Bad Request for invalid ID format on update
    Map<String, Double> prices = new HashMap<>();
    prices.put("monthly", 19.99);
    Tariff tariff = new Tariff(null, "InvalidId", Arrays.asList("Basic"), prices);
    mockMvc
        .perform(
            put("/tariffs/invalid")
                .header(API_KEY_HEADER, apiKeyValue)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tariff)))
        // .andDo(print())
        .andExpect(status().isBadRequest());
  }

  @Test
  void testUpdateTariffWithNullBody() throws Exception {
    // Should return 400 Bad Request for null request body on update
    mockMvc
        .perform(
            put("/tariffs/1")
                .header(API_KEY_HEADER, apiKeyValue)
                .contentType(MediaType.APPLICATION_JSON))
        // .andDo(print())
        .andExpect(status().isBadRequest());
  }

  @Test
  void testDeleteTariffWithNonExistentId() throws Exception {
    // Should return 404 Not Found when deleting a non-existent tariff
    mockMvc
        .perform(delete("/tariffs/99999").header(API_KEY_HEADER, apiKeyValue)) // .andDo(print())
        .andExpect(status().isNotFound());
  }

  @Test
  void testDeleteTariffWithInvalidIdFormat() throws Exception {
    // Should return 400 Bad Request for invalid ID format on delete
    mockMvc
        .perform(delete("/tariffs/invalid").header(API_KEY_HEADER, apiKeyValue)) // .andDo(print())
        .andExpect(status().isBadRequest());
  }

  // --- Invalid API key tests ---
  @Test
  void createTariffWithInvalidApiKey() throws Exception {
    String invalidKey = java.util.UUID.randomUUID().toString();
    Map<String, Double> prices = new HashMap<>();
    prices.put("monthly", 19.99);
    Tariff tariff = new Tariff(null, "Basic Tariff", Arrays.asList("Fast", "Secure"), prices);
    mockMvc
        .perform(
            post("/tariffs")
                .header(API_KEY_HEADER, invalidKey)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tariff)))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }

  @Test
  void getTariffWithInvalidApiKey() throws Exception {
    String invalidKey = java.util.UUID.randomUUID().toString();
    mockMvc
        .perform(get("/tariffs/1").header(API_KEY_HEADER, invalidKey))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }

  @Test
  void updateTariffWithInvalidApiKey() throws Exception {
    String invalidKey = java.util.UUID.randomUUID().toString();
    Map<String, Double> prices = new HashMap<>();
    prices.put("monthly", 19.99);
    Tariff tariff = new Tariff(null, "Basic Tariff", Arrays.asList("Fast", "Secure"), prices);
    mockMvc
        .perform(
            put("/tariffs/1")
                .header(API_KEY_HEADER, invalidKey)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tariff)))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }

  @Test
  void deleteTariffWithInvalidApiKey() throws Exception {
    String invalidKey = java.util.UUID.randomUUID().toString();
    mockMvc
        .perform(delete("/tariffs/1").header(API_KEY_HEADER, invalidKey))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }
}
