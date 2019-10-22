package me.encast.clara.boss;

import me.encast.clara.entity.ClaraEntityBoss;
import me.encast.clara.entity.ClaraEntitySubType;
import me.encast.clara.util.hologram.ClaraEntityHologram;
import me.encast.clara.util.hologram.Hologram;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Sephrem extends EntityPigZombie implements ClaraEntityBoss {

    private static final org.bukkit.inventory.ItemStack[] ITEMS = new org.bukkit.inventory.ItemStack[4];
    private static DecimalFormat FORMAT = new DecimalFormat("#");

    private Incarnation incarnation = Incarnation.getRandom();

    private int tick;

    private ClaraEntityHologram hologram;

    static {
        ITEMS[3] = new org.bukkit.inventory.ItemStack(org.bukkit.Material.LEATHER_HELMET);
        ITEMS[2] = new org.bukkit.inventory.ItemStack(org.bukkit.Material.GOLD_CHESTPLATE);
        ITEMS[1] = new org.bukkit.inventory.ItemStack(org.bukkit.Material.LEATHER_LEGGINGS);
        ITEMS[0] = new org.bukkit.inventory.ItemStack(org.bukkit.Material.LEATHER_BOOTS);
    }

    public Sephrem(World world) {
        super(world);
        setInvisible(false);

        ((LivingEntity) getBukkitEntity()).getEquipment().setArmorContents(ITEMS);
        ((LivingEntity) getBukkitEntity()).getEquipment().setItemInHand(new org.bukkit.inventory.ItemStack(org.bukkit.Material.DIAMOND_SWORD));
        ((LivingEntity) getBukkitEntity()).getEquipment().setItemInHandDropChance(0.5f);
        getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.6d);
        getAttributeInstance(GenericAttributes.maxHealth).setValue(1000.0d);
        getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).setValue(15.0d);
        getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(40d);
        getAttributeInstance(GenericAttributes.c).setValue(0d);

        hologram = new ClaraEntityHologram(
                new Location(world.getWorld(), locX, locY, locZ),
                this
        );
    }

    @Override
    public String getName() {
        return "Enraged Sephrem (Dark Incarnation)";
    }

    @Override
    public ClaraEntitySubType getSubType() {
        return ClaraEntitySubType.FLOOR_BOSS;
    }

    @Override
    public int getCurrentTick() {
        return tick;
    }

    @Override
    public Hologram getHologram() {
        return hologram;
    }

    @Override
    public EntityLiving getEntity() {
        return this;
    }

    @Override
    public Incarnation getIncarnation() {
        return incarnation;
    }

    // Whether the anger level is greater than 0
    @Override
    public boolean cm() {
        return true;
    }

    // Ticks entity
    @Override
    public void K() {
        super.K();

        tick++;

        hologram.update();

        if(tick % 10 != 0)
            return;

        // Flame circle
        double radius = 1.5;
        for(int i = 0; i < 361; i += 2) {
            double x = locX + radius * Math.cos(i);
            double z = locZ + radius * Math.sin(i);
            Bukkit.getWorlds().get(0).spigot().playEffect(new Location(Bukkit.getWorlds().get(0), x, locY, z), Effect.FLAME, 0, 0, 0f, 0f, 0f, 0f, 1, 25);
            if(Math.random() <= 0.05)
                Bukkit.getWorlds().get(0).spigot().playEffect(new Location(Bukkit.getWorlds().get(0),
                        x + (Math.random() <= 0.5 ? Math.random() : -Math.random()),
                        locY + 2.2,
                        z + (Math.random() <= 0.5 ? Math.random() : -Math.random())), Effect.LAVADRIP,
                        0, 0, 0f, 0f, 0f, 0f, 1, 25);
        }

        for(org.bukkit.entity.Entity e : getBukkitEntity().getNearbyEntities(25, 25, 25)) {
            if(e instanceof Player) {
                Player p = (Player) e;
                hologram.show(p);
                if(p.getGameMode() == GameMode.CREATIVE || p.getGameMode() == GameMode.SPECTATOR)
                    continue;
                Location loc = e.getLocation();
                Bukkit.getWorlds().get(0).spigot().playEffect(
                        new Location(Bukkit.getWorlds().get(0), loc.getX(), loc.getY() + 2.2, loc.getZ()),
                        Effect.WITCH_MAGIC,
                        0, 0, 0f, 0f, 0f, 0f, 1, 25
                );
            }
        }
    }

    @Override
    public void die() {
        super.die();
        hologram.hideAndDestroy();
        org.bukkit.World w = Bukkit.getWorld(world.getWorld().getUID());
        Location loc = new Location(w, locX, locY, locZ);
        w.createExplosion(locX, locY, locZ, 5, false, false);
        w.strikeLightningEffect(loc);
        w.spigot().playEffect(loc, Effect.FLAME, 0, 0, 0f, 0f, 0f, 1f, 250, 25);
        Bukkit.broadcastMessage("§b--------------------------------------");
        Bukkit.broadcastMessage("\n§c§lBOSS §5Sephrem §edefeated in §a" + (tick / 20) + " seconds§e!");
        Bukkit.broadcastMessage(" ");
        Bukkit.broadcastMessage("§b• §5§lVIEW COMBAT LOG");
        Bukkit.broadcastMessage("§b• §6§lCOLLECT REWARDS");
        Bukkit.broadcastMessage("\n§b--------------------------------------");
    }

    public static void add() {
        try {
            Method a = EntityTypes.class.getDeclaredMethod("a", Class.class, String.class, int.class);
            a.setAccessible(true);
            a.invoke(null, Sephrem.class, "Sephrem", 989);

            // add to maps e and f
//            Field c = EntityTypes.class.getDeclaredField("c");
//            c.setAccessible(true);
//            ((Map<String, Class<? extends Entity>>) c.get(null)).put("Sephrem", Sephrem.class);
//
//            Field d = EntityTypes.class.getDeclaredField("d");
//            d.setAccessible(true);
//            ((Map<Class<? extends Entity>, String>) d.get(null)).put(Sephrem.class, "Sephrem");
//
//            Field e = EntityTypes.class.getDeclaredField("e");
//            e.setAccessible(true);
//            ((Map<Integer, Class<? extends Entity>>) e.get(null)).put(989, Sephrem.class);
//
            Field f = EntityTypes.class.getDeclaredField("f");
            f.setAccessible(true);
            ((Map<Class<? extends Entity>, Integer>) f.get(null)).put(Sephrem.class, 57);

//            for (BiomeBase biomeBase : BiomeBase.getBiomes()){
//                if (biomeBase == null){
//                    break;
//                }
//
//                for (String field : new String[]{"at", "au", "av", "aw"}){
//                    try {
//                        Field list = BiomeBase.class.getDeclaredField(field);
//                        list.setAccessible(true);
//                        @SuppressWarnings("unchecked")
//                        List<BiomeBase.BiomeMeta> mobList = (List<BiomeBase.BiomeMeta>) list.get(biomeBase);
//
//                        for (BiomeBase.BiomeMeta meta : mobList){
//                            meta.b = Sephrem.class;
//                        }
//                    } catch (Exception e){
//                        e.printStackTrace();
//                    }
//                }
//            }
    } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void registerEntity(final String name, final int id, final Class<? extends Entity> nmsClass,
                                      final Class<? extends Entity> customClass) {
        try {
            final List<Map<?, ?>> dataMaps = new ArrayList<>();
            System.out.println("MAP: " + Map.class.getSimpleName());
            for (Field f : EntityTypes.class.getDeclaredFields()) {
                System.out.println(f.getName() + ", type: " + f.getType().getSimpleName());
                if (f.getType().getSimpleName().equals(Map.class.getSimpleName())) {
                    System.out.println(f.getType().getSimpleName());
                    f.setAccessible(true);
                    dataMaps.add((Map<?, ?>) f.get(null));
                }
            }
//            EntityTypes.b.a(id, new MinecraftKey(name), customClass);
            for (Field f : BiomeBase.class.getDeclaredFields()) {
                if (f.getType().getSimpleName().equals(BiomeBase.class.getSimpleName())) {
                    if (f.get(null) != null) {
                        for (Field list : BiomeBase.class.getDeclaredFields()) {
                            if (list.getType().getSimpleName().equals(List.class.getSimpleName())) {
                                list.setAccessible(true);
                                @SuppressWarnings("unchecked")
                                List<BiomeBase.BiomeMeta> metaList = (List<BiomeBase.BiomeMeta>) list.get(f.get(null));

                                for (BiomeBase.BiomeMeta meta : metaList) {
                                    Field clazz = BiomeBase.BiomeMeta.class.getDeclaredFields()[0];
                                    if (clazz.get(meta).equals(nmsClass))
                                        clazz.set(meta, customClass);
                                }
                            }
                        }
                    }
                }
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }
}
