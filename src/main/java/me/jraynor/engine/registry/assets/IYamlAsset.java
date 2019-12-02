package me.jraynor.engine.registry.assets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.File;

public interface IYamlAsset<T> {
    default T parseYaml(File file, Class type) {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            T user = (T) mapper.readValue(file, type);
            return user;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
