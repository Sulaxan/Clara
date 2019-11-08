package me.encast.clara.util.nbt;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.server.v1_8_R3.NBTBase;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagList;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
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
                this.map.put(entry.getKey(), new ObjectMap(base.getTypeId(), data.get(base), data.getType().getName()));
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

                Object obj;
                String clazz = entry.getValue().instanceClass;
                if(clazz.equalsIgnoreCase("int")) {
                    obj = ((Double) entry.getValue().data).intValue();
                } else if(clazz.equalsIgnoreCase("byte")) {
                    obj = ((Double) entry.getValue().data).byteValue();
                } else if(clazz.equalsIgnoreCase("short")) {
                    obj = ((Double) entry.getValue().data).shortValue();
                } else {
                    Class<?> instance = Class.forName(entry.getValue().instanceClass);
                    obj = instance.cast(entry.getValue().data);
                }
                data.set(base, obj);

                tag.set(entry.getKey(), base);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return tag;
    }

    private NBTTagList convertNBTList(ObjectMap map) throws Exception {
        Method createTagMethod = NBTBase.class.getDeclaredMethod("createTag", byte.class);
        createTagMethod.setAccessible(true);
        NBTBase base = (NBTBase) createTagMethod.invoke(null, map.type);
        Field data;

        NBTTagList list = new NBTTagList();
        String clazz = map.getClass().getName();

        Object obj = null;
        if(clazz.equalsIgnoreCase("int")) {
            obj = ((Double) map.data).intValue();
        } else if(clazz.equalsIgnoreCase("byte")) {
            obj = ((Double) map.data).byteValue();
        } else if(clazz.equalsIgnoreCase("short")) {
            obj = ((Double) map.data).shortValue();
        } else if(clazz.equalsIgnoreCase("java.util.List")) {
            for(ObjectMap b : (List<ObjectMap>) map.data) {
                base = convertNBTList(b);
            }
        } else {
            Class<?> instance = Class.forName(map.instanceClass);
            obj = instance.cast(map.data);
        }
        if(obj != null) {
            data = base.getClass().getDeclaredField("data");
            data.setAccessible(true);
            data.set(base, obj);
        }
        list.add(base);
        return list;
    }

    private Object getData(NBTBase base) throws Exception {
        Field data = base.getClass().getDeclaredField("data");
        data.setAccessible(true);
        return data.get(base);
    }

    private List<ObjectMap> recurseList(NBTTagList list) throws Exception {
        List<ObjectMap> objs = Lists.newArrayList();
        Field data = list.getClass().getDeclaredField("list");
        data.setAccessible(true);
        for(NBTBase base : ((List<NBTBase>) data.get(list))) {
            Object obj;
            if(base instanceof NBTTagList) {
                obj = recurseList((NBTTagList) base);
            } else {
                obj = getData(base);
            }
            objs.add(new ObjectMap(base.getTypeId(), obj, obj.getClass().getName()));
        }

        return objs;
    }

    public class ObjectMap {

        private byte type;
        private Object data;
        private String instanceClass;

        public ObjectMap() {
        }

        public ObjectMap(byte type, Object data, String instanceClass) {
            this.type = type;
            this.data = data;
            this.instanceClass = instanceClass;
        }
    }
}
