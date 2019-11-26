package me.encast.clara.util.resource;

public class JsonResourceCluster extends ResourceCluster<JsonResourceLoader> {

    // Preventing
    private static final String[] EMPTY_ARRAY = new String[0];

    public JsonResourceCluster() {
    }

    public String[] getMultiline(String resourceKey, String key, Object... args) {
        JsonResourceLoader loader = getResource(resourceKey);
        if(loader != null) {
            return loader.getMultiline(key, args);
        } else if(getDefaultLoader() != null) {
            return getDefaultLoader().getMultiline(key, args);
        }
        return EMPTY_ARRAY;
    }

    public String[] getMultiline(Locale locale, String key, Object... args) {
        return getMultiline(locale.getKey(), key, args);
    }
}
