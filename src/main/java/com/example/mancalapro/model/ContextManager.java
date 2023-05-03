package com.example.mancalapro.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages a shared context for storing and retrieving objects.
 * 
 * @author Olabayoji Oladepo
 */
public class ContextManager {
    private Map<String, Object> context;
    public static ContextManager instance;

    /**
     * Constructs a new ContextManager with an empty context.
     */
    private ContextManager() {
        context = new HashMap<>();
    }

    /**
     * Returns the singleton instance of ContextManager.
     * 
     * @return The instance of ContextManager.
     */
    public static ContextManager getInstance() {
        if (instance == null) {
            instance = new ContextManager();
        }
        return instance;
    }

    /**
     * Retrieves an object from the context by its key.
     * 
     * @param key The key associated with the object.
     * @return The object or null if not found.
     */
    public Object retrieveFromContext(String key) {
        return context.get(key);
    }

    /**
     * Adds an object to the context with the given key.
     * 
     * @param key   The key associated with the object.
     * @param value The object to store in the context.
     */
    public void addToContext(String key, Object value) {
        context.put(key, value);
    }
}
