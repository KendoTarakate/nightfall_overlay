package net.nightfall;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * Loader-agnostic persistent nickname storage. Touches no Minecraft or
 * mod-loader classes, so it is compiled into both the Fabric and the Forge
 * artifacts and deduplicated in the universal jar.
 */
public class NickStorage {
    private static final File FILE = new File("config/nicknames.json");
    private static final Gson GSON = new Gson();
    private static final Map<UUID, String> nicknames = new HashMap<>();

    // Optional hook fired (with the affected UUID) whenever a nickname changes.
    // The Plasmo Voice addon registers this to refresh the voice overlay live;
    // it stays null when Plasmo Voice is absent, so the command code needs no
    // reference to Plasmo Voice.
    private static volatile Consumer<UUID> changeListener;

    public static void setChangeListener(Consumer<UUID> listener) {
        changeListener = listener;
    }

    private static void fireChange(UUID uuid) {
        Consumer<UUID> listener = changeListener;
        if (listener != null) {
            try {
                listener.accept(uuid);
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }

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
            if (rawMap != null) {
                for (Map.Entry<String, String> entry : rawMap.entrySet()) {
                    nicknames.put(UUID.fromString(entry.getKey()), entry.getValue());
                }
            }
            // After a (re)load, refresh the voice overlay of any affected online
            // player (no-op at startup before the listener is registered).
            for (UUID changed : nicknames.keySet()) {
                fireChange(changed);
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getNickname(UUID uuid) {
        return nicknames.get(uuid);
    }

    public static void setNicknames(UUID uuid, String nickname) {
        nicknames.put(uuid, nickname);
        save();
        fireChange(uuid); // refresh the voice overlay (if Plasmo Voice is present)
    }
}
