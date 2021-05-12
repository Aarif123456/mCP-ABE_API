package com.gcp;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class MapLoader {
    public static Map<String, String> getLoadMap(String mapJson, Gson gson) {
        Type collectionType = new HashMapTypeToken().getType();
        return gson.fromJson(mapJson, collectionType);
    }

    private static class HashMapTypeToken extends TypeToken<HashMap<String, String>> {
    }
}