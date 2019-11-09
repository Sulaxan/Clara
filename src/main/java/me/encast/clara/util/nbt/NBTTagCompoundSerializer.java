package me.encast.clara.util.nbt;

import com.google.gson.*;

import java.lang.reflect.Type;

public class NBTTagCompoundSerializer implements JsonSerializer, JsonDeserializer {

    private static final String CLASS_PROPERTY_KEY = "inst_class";
    private static final String DATA_PROPERTY_KEY = "inst_data";

    @Override
    public Object deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();
        JsonPrimitive classPrim = obj.getAsJsonPrimitive(CLASS_PROPERTY_KEY);
        String className = classPrim.getAsString();
        try {
            Class<?> clazz = Class.forName(className);
            return context.deserialize(obj.get(DATA_PROPERTY_KEY), clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public JsonElement serialize(Object src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();
        obj.addProperty(CLASS_PROPERTY_KEY, src.getClass().getName());
        obj.add(DATA_PROPERTY_KEY, context.serialize(src));
        return obj;
    }
}
