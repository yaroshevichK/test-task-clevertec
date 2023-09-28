package task.clevertec.util;

import lombok.Getter;
import org.yaml.snakeyaml.Yaml;

import java.util.Map;

import static task.clevertec.util.Constants.PROPERTY_FILE;

@Getter
public class Configuration {
    private static final Map<String, Map<String, Object>> properties;

    static {
        Yaml yaml = new Yaml();
        properties = yaml
                .load(Configuration
                        .class
                        .getClassLoader()
                        .getResourceAsStream(PROPERTY_FILE)
                );
    }

    public static <T> T getProperty(String root, String key) {
        Map<String, Object> values = properties.get(root);
        return (T) values.get(key);
    }
}
