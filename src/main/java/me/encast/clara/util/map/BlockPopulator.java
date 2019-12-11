package me.encast.clara.util.map;

public enum BlockPopulator {

    ;

    public static BlockPopulator[] VALUES = values();

    public static BlockPopulator match(String name) {
        for(BlockPopulator populator : VALUES) {
            if(populator.name().equalsIgnoreCase(name))
                return populator;
        }

        return null;
    }
}
