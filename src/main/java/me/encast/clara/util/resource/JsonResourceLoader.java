package me.encast.clara.util.resource;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;

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
    public String get(String key, Object... args) {
        if(object.has(key)) {
            String s = object.get(key).getAsString();
            if(args != null && args.length > 0) {
                for(Object arg : args) {
                    s = REPLACE_PATTERN.matcher(s).replaceFirst(arg.toString());
                }
            }
            return s;
        }
        return null;
    }

    public String[] getMultiline(String key, Object... args) {
        if(object.has(key)) {
            JsonArray array = object.get(key).getAsJsonArray();
            if(array != null) {
                String[] strings = new String[array.size()];
                int i = 0;
                Iterator<JsonElement> iterator = array.iterator();
                while(iterator.hasNext()) {
                    strings[i] = iterator.next().getAsString();
                    i++;
                }

                if(args != null && args.length > 0)
                    strings = format(strings, args);

                return strings;
            }
        }
        return null;
    }

    private String[] format(String[] strings, Object... args) {
        int i;
        if(strings != null && args != null && args.length >= 1) {
            for(Object arg : args) {
                for(i = 0; i < strings.length; i++) {
                    String newLine = REPLACE_PATTERN.matcher(strings[i]).replaceFirst(arg.toString());
                    if(!strings[i].equals(newLine)) {
                        strings[i] = newLine;
                        break;
                    }
                }
            }
        }
        return strings;
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
