package me.encast.clara.util.resource;

import java.util.regex.Pattern;

public interface ResourceLoader {

    Pattern REPLACE_PATTERN = Pattern.compile("%%[a-zA-Z0-9]*%%");

    String get(String key, Object... args);

    int getInt(String key);

    long getLong(String key);

    double getDouble(String key);

    float getFloat(String key);
}
