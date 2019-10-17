package me.encast.clara.util.inventory;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Defines the size of an {@link AbstractInventory} inventory.
 */
@AllArgsConstructor
@Getter
public enum InvSize {

    ONE_ROW(9),
    TWO_ROWS(18),
    THREE_ROWS(27),
    FOUR_ROWS(36),
    FIVE_ROWS(45),
    SIX_ROWS(54);

    private int slots;

    public static InvSize match(int value, InvSize defaultValue) {
        for(InvSize size : values()) {
            if(size.getSlots() == value)
                return size;
        }

        return defaultValue;
    }
}
