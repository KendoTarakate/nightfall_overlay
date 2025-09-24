package net.nightfall;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NickStorage {
    private static final File FILE = new File("config/nicknames.json");
    private static final Gson GSON = new Gson();
    private static final Map<UUID, String> nicknames = new HashMap<>();



    public static void load() {
        try {
            if (!FILE.exists()) {
                FILE.getParentFile().mkdirs();
                save();
                return;
            }

            InputStreamReader reader = new InputStreamReader(
                    new FileInputStream(FILE), StandardCharsets.UTF_8
            );

            Type type = new TypeToken<Map<String, String>>() {}.getType();
            Map<String, String> rawMap = GSON.fromJson(reader, type);
            reader.close();

            nicknames.clear();
            for (Map.Entry<String, String> entry : rawMap.entrySet()) {
                nicknames.put(UUID.fromString(entry.getKey()), entry.getValue());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void save() {
        try {
            Map<String, String> saveMap = new HashMap<>();
            for (Map.Entry<UUID, String> entry : nicknames.entrySet()) {
                saveMap.put(entry.getKey().toString(), entry.getValue());
            }

            OutputStreamWriter writer = new OutputStreamWriter(
                    new FileOutputStream(FILE), StandardCharsets.UTF_8
            );
            GSON.toJson(saveMap, writer);
            writer.close();

            System.out.println("Saved nicknames: " + saveMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getNickname(UUID uuid) {
        System.out.println("Loading nickname for: " + uuid + " -> " + nicknames);
        return nicknames.get(uuid);
    }
    public static void setNicknames(UUID uuid, String nickname) {
        nicknames.put(uuid,nickname);
        System.out.println("Saving nickname" + uuid + "->" + nickname);
        save();
    }
}