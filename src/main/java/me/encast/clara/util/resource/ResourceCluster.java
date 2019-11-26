package me.encast.clara.util.resource;

import com.google.common.collect.Maps;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.Map;
import java.util.function.Function;

public class ResourceCluster {

    private Map<String, ResourceLoader> resources = Maps.newConcurrentMap();

    public ResourceCluster() {
    }

    public void addResource(String key, ResourceLoader loader) {
        this.resources.put(key, loader);
    }

    public ResourceLoader getResource(String key) {
        return this.resources.getOrDefault(key, null);
    }

    // gets all the files within the directory (recursively) and maps a resource loader to the file
    public void traverseDirectory(Path path, Function<InputStream, ResourceLoader> resourceFunc) {

    }
}
