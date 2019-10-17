package me.encast.clara.util.entity;

import net.minecraft.server.v1_8_R3.EntityArmorStand;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;

public class WrappedArmorStand extends WrappedEntity<EntityArmorStand> {

    private EntityArmorStand entity;

    public WrappedArmorStand(Location location) {
        this.entity = new EntityArmorStand(((CraftWorld) location.getWorld()).getHandle());
        this.entity.setLocation(
                location.getX(),
                location.getY(),
                location.getZ(),
                location.getYaw(),
                location.getPitch()
        );
    }

    public void setGravity(boolean gravity) {
        entity.setGravity(false);
    }

    public void setVisible(boolean visible) {
        entity.setInvisible(visible);
    }

    @Override
    public EntityArmorStand getEntity() {
        return entity;
    }
}
