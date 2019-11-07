package me.encast.clara.item;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.NBTTagCompound;

@AllArgsConstructor
@Getter
public class SaveableItem {

    private int slot;
    private NBTTagCompound item;
}
