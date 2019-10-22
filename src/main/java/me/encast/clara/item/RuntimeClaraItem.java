package me.encast.clara.item;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.NBTTagCompound;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class RuntimeClaraItem {

    private UUID uuid;
    private ClaraItem item;
    private NBTTagCompound nbt;
}
