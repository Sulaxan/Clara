package me.encast.clara;

import me.encast.clara.util.resource.ResourceLoader;

import java.util.Arrays;

public class Test {

    public static void main(String[] args) {
        String[] s = {
                " ",
                "§7Summary of your stats:",
                " ",
                "§cHealth: §7%%hp%%",
                "§9Defense: §7%%def%%%",
                " ",
                "§6You have learned a total of",
                "§6%%count%% spells!",
                " ",
                "§7%%count%% piece(s) of armor equipped!",
                "§7%%count%% item(s) in your inventory!"
        };
        s = format(s, 1100, 0.02 * 100, 5, 5, 5);
        System.out.println(Arrays.toString(s));
    }

    private static String[] format(String[] strings, Object... args) {
        int i;
        if(strings != null && args != null && args.length >= 1) {
            for(Object arg : args) {
                for(i = 0; i < strings.length; i++) {
                    String newLine = ResourceLoader.REPLACE_PATTERN.matcher(strings[i]).replaceFirst(arg.toString());
                    if(!strings[i].equals(newLine)) {
                        strings[i] = newLine;
                        break;
                    }
                }
            }
        }
        return strings;
    }
}
