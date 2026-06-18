package com.restaurant.utility;

import java.io.*;
import java.util.*;

/**
 * LocalStorage simulation for Java Desktop Application.
 * Stores data in local_storage.json in the workspace root.
 */
public class LocalStorage {
    private static final String FILE_PATH = "local_storage.json";
    private static final Map<String, String> store = new LinkedHashMap<>();

    static {
        load();
    }

    private static synchronized void load() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return;
        }
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            reader.close();
            
            ArrayList<LinkedHashMap<String, String>> list = JsonHelper.parseJsonArray("[" + sb.toString() + "]");
            if (!list.isEmpty()) {
                store.putAll(list.get(0));
            }
        } catch (Exception e) {
            System.err.println("Failed to load local storage: " + e.getMessage());
        }
    }

    private static synchronized void save() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH));
            writer.write(JsonHelper.toJson(store));
            writer.close();
        } catch (Exception e) {
            System.err.println("Failed to save local storage: " + e.getMessage());
        }
    }

    public static synchronized void setItem(String key, String value) {
        store.put(key, value);
        save();
    }

    public static synchronized String getItem(String key) {
        return store.get(key);
    }

    public static synchronized void removeItem(String key) {
        store.remove(key);
        save();
    }

    public static synchronized void clear() {
        store.clear();
        save();
    }
}
