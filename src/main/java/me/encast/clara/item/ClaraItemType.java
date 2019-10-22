package me.encast.clara.item;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.encast.clara.armor.impl.CrysalisHelmet;

import java.util.concurrent.Callable;

@AllArgsConstructor
public enum ClaraItemType {

    CRYSALIS_HELMET(CrysalisHelmet.ID, CrysalisHelmet::new);

    public static final ClaraItemType[] VALUES = values();

    @Getter
    private String id;
    private Callable<ClaraItem> constructCallable;

    public ClaraItem getItem() {
        try {
            return constructCallable.call();
        } catch (Exception e) {
            return null;
        }
    }
}
