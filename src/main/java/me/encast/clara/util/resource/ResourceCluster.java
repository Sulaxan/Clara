package me.encast.clara.util.resource;

import com.google.common.collect.Maps;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Stream;

public class ResourceCluster<T extends ResourceLoader> {

    private Map<String, T> resources = Maps.newConcurrentMap();
    private T defaultLoader;

    public ResourceCluster() {
    }

    public void addResource(String key, T loader) {
        this.resources.put(key, loader);
    }

    public T getResource(String key) {
        return this.resources.getOrDefault(key, null);
    }

    public T getDefaultLoader() {
        return defaultLoader;
    }

    public void setDefaultLoader(T defaultLoader) {
        this.defaultLoader = defaultLoader;
    }

    // will try to make the default loader the loader mapped to key, or null if it doesn't exist
    public void makeDefault(String key) {
        this.defaultLoader = getResource(key);
    }

    public String get(String resourceKey, String key, Object... args) {
        ResourceLoader loader = getResource(resourceKey);
        if(loader != null) {
            return loader.get(key, args);
        } else if(defaultLoader != null) {
            return defaultLoader.get(key, args);
        }
        return "";
    }

    public String get(Locale locale, String key, Object... args) {
        return get(locale.getKey(), key, args);
    }

    // gets all the files within the directory (recursively) and maps a resource loader to the file
    public void traverseDirectory(Path path, BiFunction<String, InputStream, T> resourceFunc) {
        try {
            Stream<Path> walk = Files.walk(path);
            Iterator<Path> iterator = walk.iterator();
            while(iterator.hasNext()) {
                Path p = iterator.next();
                if(Files.isDirectory(p))
                    continue;
                if(p.getNameCount() > 1) {
                    String name = p.getFileName().toString();
                    T loader = resourceFunc.apply(name, Files.newInputStream(p));
                    if(loader != null) {
                        addResource(name.split("\\.")[0], loader);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
