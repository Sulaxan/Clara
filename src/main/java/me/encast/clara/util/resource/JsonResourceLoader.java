package me.encast.clara.util.resource;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.InputStream;
import java.io.InputStreamReader;

public class JsonResourceLoader implements ResourceLoader {

    private static final Gson GSON_INSTANCE = new Gson();

    private JsonObject object;

    public JsonResourceLoader(InputStream stream) {
        try {
            InputStreamReader reader = new InputStreamReader(stream);
            object = GSON_INSTANCE.fromJson(reader, JsonObject.class);
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String get(String key) {
        if(object.has(key))
            return object.get(key).getAsString();
        return null;
    }

    @Override
    public String getAndFormat(String key, Object... args) {
        String val = get(key);
        if(val != null && args != null && args.length >= 1) {
            for(Object arg : args) {
                val = REPLACE_PATTERN.matcher(val).replaceFirst(arg.toString());
            }
        }
        return val;
    }

    public static JsonResourceLoader fromJAR(String path) {
        try {
            return new JsonResourceLoader(JsonResourceLoader.class.getResourceAsStream(path));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
