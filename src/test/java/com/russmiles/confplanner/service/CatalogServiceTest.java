package com.russmiles.confplanner.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.russmiles.confplanner.domain.Session;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The catalog is plain data, so we test it as plain data: it loads, it is the size we expect,
 * and &mdash; crucially for Lab 3 &mdash; it contains deliberate time-slot clashes for the
 * double-booking guardrail to catch.
 */
class CatalogServiceTest {

    private final CatalogService service = new CatalogService(new ObjectMapper());

    @Test
    void loadsTheSyntheticCatalog() {
        var catalog = service.catalog();
        assertTrue(catalog.sessions().size() >= 30, "expected ~30+ sessions");
        assertTrue(catalog.speakers().size() >= 8, "expected several speakers");
        var tracks = catalog.sessions().stream().map(Session::track).collect(Collectors.toSet());
        assertTrue(tracks.size() >= 4, "expected at least 4 tracks, got " + tracks);
    }

    @Test
    void containsDeliberateSlotClashes() {
        var bySlot = service.catalog().sessions().stream()
                .collect(Collectors.groupingBy(Session::slot, Collectors.counting()));
        boolean hasClash = bySlot.values().stream().anyMatch(count -> count > 1);
        assertTrue(hasClash, "catalog must contain overlapping slots for the guardrail lab: "
                + bySlot.entrySet().stream().filter(e -> e.getValue() > 1)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
        assertFalse(service.catalog().sessions().isEmpty());
    }
}
