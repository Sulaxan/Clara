package me.encast.clara.util;

import com.google.common.collect.Lists;
import me.encast.clara.Clara;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class Util {

    // Prevent initialization
    private Util() {
    }

    public static List<String> formatStringToList(String s, int charsPerLine) {
        String[] split = s.split(" ");
        List<String> list = Lists.newArrayList();
        StringBuilder append = new StringBuilder();

        int count = 0;
        for(String word : split) {
            if((word.toCharArray().length + count) >= charsPerLine) {
                list.add(append.toString().trim());
                append = new StringBuilder();
                count = 0;
            }
            append.append(word).append(" ");
            count += word.toCharArray().length;
            if(word.equals(split[split.length - 1])) {
                list.add(append.toString().trim());
            }
        }
        return list;
    }

    public static <T> T loadResourceFromJar(String path, Class<T> clazz) {
        T t = null;
        try (InputStreamReader reader = new InputStreamReader(Clara.getInstance().getResource(path))) {
            t = Clara.GSON.fromJson(reader, clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return t;
    }
}
