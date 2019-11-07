package me.encast.clara.util.nbt;

import com.google.common.collect.Maps;
import net.minecraft.server.v1_8_R3.NBTBase;
import net.minecraft.server.v1_8_R3.NBTTagCompound;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

public class SaveableNBT {

    private Map<String, ObjectMap> map = Maps.newConcurrentMap();

    public SaveableNBT() {
    }

    public SaveableNBT(NBTTagCompound tag) {
        from(tag);
    }

    public void from(NBTTagCompound tag) {
        try {
            Field mapField = tag.getClass().getDeclaredField("map");
            mapField.setAccessible(true);
            Map<String, NBTBase> map = (Map<String, NBTBase>) mapField.get(tag);

            for(Map.Entry<String, NBTBase> entry : map.entrySet()) {
                NBTBase base = entry.getValue();
                Field data = base.getClass().getDeclaredField("data");
                data.setAccessible(true);
                this.map.put(entry.getKey(), new ObjectMap(base.getTypeId(), data.get(base)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public NBTTagCompound toNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        for(Map.Entry<String, ObjectMap> entry : map.entrySet()) {
            try {
                Method createTagMethod = NBTBase.class.getDeclaredMethod("createTag", byte.class);
                createTagMethod.setAccessible(true);
                NBTBase base = (NBTBase) createTagMethod.invoke(null, entry.getValue().type);

                Field data = base.getClass().getDeclaredField("data");
                data.setAccessible(true);
                data.set(base, entry.getValue().data);

                tag.set(entry.getKey(), base);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return tag;
    }

    public class ObjectMap {

        private byte type;
        private Object data;

        public ObjectMap() {
        }

        public ObjectMap(byte type, Object data) {
            this.type = type;
            this.data = data;
        }
    }
}
