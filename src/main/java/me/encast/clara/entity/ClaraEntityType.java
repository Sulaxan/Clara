package me.encast.clara.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

@AllArgsConstructor
@Getter
public enum ClaraEntityType {

    ;

    private static final ClaraEntityType[] VALUES = values();

    private String name;
    private Class<? extends EntityLiving> entityClass;
    private int id;
    private EntityType type;

    public void spawn(Location loc) {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("id", name);
        CraftWorld world = (CraftWorld) loc.getWorld();
        Entity entity = EntityTypes.a(id, world.getHandle());
        entity.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        world.addEntity(entity, CreatureSpawnEvent.SpawnReason.CUSTOM);
    }

    public static void registerAll() {
        try {
            // Method that registers an entity
            Method a = EntityTypes.class.getDeclaredMethod("a", Class.class, String.class, int.class);
            a.setAccessible(true);
            Field f = EntityTypes.class.getDeclaredField("f");
            f.setAccessible(true);
            // entity class -> type id mapping (this must be a valid id of an existing entity)
            Map<Class<? extends Entity>, Integer> entityToIdMappings =
                    ((Map<Class<? extends Entity>, Integer>) f.get(null));
            for (ClaraEntityType type : VALUES) {
                try {
                    type.getEntityClass().asSubclass(ClaraEntity.class);
                } catch (Exception e) {
                    System.err.println("Entity " + type.getName() + " must implement ClaraEntity");
                }
                a.invoke(null, type.getEntityClass(), type.getName(), type.getId());
                // Adding as a valid entity
                entityToIdMappings.put(type.getEntityClass(), (int) type.getType().getTypeId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
