package me.jraynor.core.config;

import com.google.gson.*;
import me.jraynor.core.context.Context;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Holds various global configuration information that the user can modify.
 * It can be serialized and deserialize to/from yaml
 */
public class Config {
    private Logger logger = LogManager.getLogger();    private RootConfig config;
    private Context context;

    public Config(Context context) {
        this.context = context;
    }

    public JsonObject loadDefaultToJson() {
        try (Reader baseReader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/default.cfg")))) {
            return JsonParser.parseReader(baseReader).getAsJsonObject();
        } catch (IOException e) {
            throw new RuntimeException("Missing default configuration file");
        }
    }

    public void loadDefaults() {
        JsonObject jsonConfig = loadDefaultToJson();
//        config = createGson().fromJson(jsonConfig, RootConfig.class);
    }


    public Optional<JsonObject> loadFileToJson(Path configPath) {
        if (Files.isRegularFile(configPath)) {
            try (Reader reader = Files.newBufferedReader(configPath, UTF_8)) {
                JsonElement userConfig = new JsonParser().parse(reader);
                if (userConfig.isJsonObject()) {
                    return Optional.of(userConfig.getAsJsonObject());
                }
            } catch (IOException e) {
                logger.error("Failed to load config file {}, falling back on default config", configPath);
            }
        }
        return Optional.empty();
    }


    private static void merge(JsonObject target, JsonObject from) {
        for (Map.Entry<String, JsonElement> entry : from.entrySet()) {
            if (entry.getValue().isJsonObject()) {
                if (target.has(entry.getKey()) && target.get(entry.getKey()).isJsonObject()) {
                    merge(target.get(entry.getKey()).getAsJsonObject(), entry.getValue().getAsJsonObject());
                } else {
                    target.remove(entry.getKey());
                    target.add(entry.getKey(), entry.getValue());
                }
            } else {
                target.remove(entry.getKey());
                target.add(entry.getKey(), entry.getValue());
            }
        }
    }

}
