package me.encast.clara.util.resource;

public class JsonResourceCluster extends ResourceCluster<JsonResourceLoader> {

    // Preventing
    private static final String[] EMPTY_ARRAY = new String[0];

    public JsonResourceCluster() {
    }

    public String[] getMultiline(String resourceKey, String key) {
        JsonResourceLoader loader = getResource(resourceKey);
        if(loader != null) {
            return loader.getMultiline(key);
        } else if(getDefaultLoader() != null) {
            return getDefaultLoader().getMultiline(key);
        }
        return EMPTY_ARRAY;
    }

    public String[] getAndFormatMultiline(String resourceKey, String key, Object... args) {
        JsonResourceLoader loader = getResource(resourceKey);
        if(loader != null) {
            return loader.getAndFormatMultiline(key, args);
        } else if(getDefaultLoader() != null) {
            return getDefaultLoader().getAndFormatMultiline(key, args);
        }
        return EMPTY_ARRAY;
    }
}
