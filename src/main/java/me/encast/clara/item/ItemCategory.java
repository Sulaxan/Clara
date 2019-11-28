package me.encast.clara.item;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ItemCategory {

    ITEM("item.category.item"),
    MENU_ITEM("item.category.menu_item"),
    ARMOR("item.category.armor");

    private String messageKey;
}
