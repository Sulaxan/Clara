package me.encast.clara.boss;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.concurrent.ThreadLocalRandom;

@AllArgsConstructor
@Getter
public enum Incarnation {

    WEAKNESS("Weakness"),
    FLOW("Flow"),
    GOD_AND_DEMON("God & Demon"),
    SINFUL("Sinful"),
    TIME("Time");

    public static final Incarnation[] VALUES = values();

    private String name;

    public static Incarnation getRandom() {
        return VALUES[ThreadLocalRandom.current().nextInt(VALUES.length)];
    }
}
