package me.jraynor.core.registry.assets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.File;

public interface IYamlAsset<T> {
    default T parseYaml(File file, Class type) {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            T user = (T) mapper.readValue(file, type);
            System.out.println(ReflectionToStringBuilder.toString(user, ToStringStyle.MULTI_LINE_STYLE));
            return user;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
