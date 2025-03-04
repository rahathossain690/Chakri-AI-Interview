package com.rahathossain.chakri.model;

import java.util.LinkedHashMap;

public class JSONData<T> extends LinkedHashMap<String, T> {

    public static <P> JSONData<P> create() {
        return new JSONData<>();
    }

    public static <P> JSONData<P> create(String key, P value) {
        JSONData<P> jsonData = new JSONData<>();
        jsonData.put(key, value);

        return jsonData;
    }

    public JSONData<T> addItem(String key, T value) {
        this.put(key, value);

        return this;
    }
}
