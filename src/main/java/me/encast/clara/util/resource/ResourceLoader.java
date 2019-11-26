package me.encast.clara.util.resource;

import java.util.regex.Pattern;

public interface ResourceLoader {

    Pattern REPLACE_PATTERN = Pattern.compile("%%[a-zA-Z0-9]*%%");

    String get(String key, Object... args);
}
