package com.example.demo.common;

import java.util.HashMap;

public class UppercasedMap extends HashMap<String, Object> {
    @Override
    public Object put(String key, Object value) {
        return super.put(key.toUpperCase(), value);
    }
}
