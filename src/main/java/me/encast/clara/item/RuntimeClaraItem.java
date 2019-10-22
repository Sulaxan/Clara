package me.encast.clara.item;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class RuntimeClaraItem {

    private UUID uuid;
    private ClaraItem item;
}
