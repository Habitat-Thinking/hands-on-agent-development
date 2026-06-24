package com.russmiles.confplanner.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.russmiles.confplanner.domain.SessionCatalog;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UncheckedIOException;

/**
 * Loads the bundled, synthetic conference catalog.
 *
 * <p>This is deliberately ordinary code: no AI, no prompts, just Jackson reading a JSON file off
 * the classpath. It exists to make a point that runs through the whole workshop &mdash; a good agent
 * is mostly plain code, and the LLM is invited in only where judgement is actually required.
 * The catalog never changes at runtime, so we read it once and cache it.
 */
@Service
public class CatalogService {

    private static final String CATALOG_PATH = "catalog/uberconf-sample-catalog.json";

    private final SessionCatalog catalog;

    public CatalogService(ObjectMapper objectMapper) {
        this.catalog = load(objectMapper);
    }

    /** The full catalog of sessions and speakers. */
    public SessionCatalog catalog() {
        return catalog;
    }

    private SessionCatalog load(ObjectMapper objectMapper) {
        try {
            var resource = new ClassPathResource(CATALOG_PATH);
            try (var in = resource.getInputStream()) {
                return objectMapper.readValue(in, SessionCatalog.class);
            }
        } catch (IOException e) {
            throw new UncheckedIOException(
                    "Could not load conference catalog from classpath: " + CATALOG_PATH, e);
        }
    }
}
