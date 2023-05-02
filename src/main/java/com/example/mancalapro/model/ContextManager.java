package com.example.mancalapro.model;

import java.util.HashMap;
import java.util.Map;

public class ContextManager {

    public static ContextManager instance;

    private ContextManager() {
        context = new HashMap<>();
    }

    public static ContextManager getInstance() {
        if (instance == null) {
            instance = new ContextManager();
        }
        return instance;
    }

    private Map<String, Object> context;

    public Object retrieveFromContext(String key) {
        return context.get(key);
    }

    public void addToContext(String key, Object value) {
        context.put(key, value);
    }
}
