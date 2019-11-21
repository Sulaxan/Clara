package me.encast.clara.util.item;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Getter;
import me.encast.clara.Clara;
import me.encast.clara.util.inventory.ItemStackUtil;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Item builder (wrapper) for Bukkit's {@link ItemStack}.
 */
@Getter
public class ItemBuilder implements Listener, Cloneable {

    private Material material;
    private ItemType itemType;
    private int amount;
    private short data;

    private String displayName;
    private List<String> lore = Lists.newArrayList();
    private Map<Enchantment, Integer> enchantments = Maps.newHashMap();
    private List<ItemFlag> itemFlags = Lists.newArrayList();
    private boolean unbreakable;

    private Color color;

    private PotionEffectType mainEffect;
    private List<PotionEffect> potionEffects = Lists.newArrayList();

    private String skullOwner;

    private Map<String, String> metadata = Maps.newHashMap();

    private Map<Action, Consumer<Player>> listeners = Maps.newConcurrentMap();

    /**
     * Constructs a new instance of ItemBuilder for internal
     * use.
     */
    private ItemBuilder() {
        Bukkit.getServer().getPluginManager().registerEvents(this, Clara.getInstance());
    }

    /**
     * Constructs a new instance of ItemBuilder.
     *
     * @param material The material for the item.
     * @param itemType The type of item.
     */
    public ItemBuilder(Material material, ItemType itemType) {
        this();
        this.material = material;
        this.itemType = itemType;
        this.amount = 1;
        this.data = (short) 0;
    }

    /**
     * Constructs a new instance of ItemBuilder using {@link ItemStack}.
     *
     * @param item The item to construct from.
     */
    public ItemBuilder(ItemStack item) {
        this();
        this.material = item.getType();
        this.amount = item.getAmount();
        this.data = item.getDurability();

        ItemMeta meta = item.getItemMeta();

        this.lore = meta.getLore();
        this.unbreakable = meta.spigot().isUnbreakable();
        this.enchantments = Maps.newHashMap(meta.getEnchants());
        this.itemFlags = Lists.newArrayList(meta.getItemFlags());

        if(meta instanceof LeatherArmorMeta) {
            LeatherArmorMeta meta2 = (LeatherArmorMeta) meta;
            this.itemType = ItemType.LEATHER_ARMOUR;
            this.color = meta2.getColor();
        } else if(meta instanceof PotionMeta) {
            PotionMeta meta2 = (PotionMeta) meta;
            this.itemType = ItemType.POTION;
            this.potionEffects = meta2.getCustomEffects();
        } else if(meta instanceof SkullMeta) {
            SkullMeta meta2 = (SkullMeta) meta;
            this.itemType = ItemType.SKULL;
            this.skullOwner = meta2.getOwner();
        } else {
            this.itemType = ItemType.NORMAL;
        }
    }

    /**
     * Sets the material for the item.
     *
     * @param material The material to set.
     * @return The instance of {@link ItemBuilder}.
     */
    public ItemBuilder setMaterial(Material material) {
        this.material = material;
        return this;
    }

    /**
     * Sets the item type for the item.
     *
     * @param itemType The item type to set.
     * @return The instance of {@link ItemBuilder}.
     */
    public ItemBuilder setItemType(ItemType itemType) {
        this.itemType = itemType;
        return this;
    }

    /**
     * Sets the amount for the item.
     *
     * @param amount The amount to set.
     * @return The instance of {@link ItemBuilder}.
     */
    public ItemBuilder setAmount(int amount) {
        this.amount = amount;
        return this;
    }

    /**
     * Sets the data/durability for the item.
     *
     * @param data The data to set.
     * @return The instance of {@link ItemBuilder}.
     */
    public ItemBuilder setData(short data) {
        this.data = data;
        return this;
    }

    /**
     * Sets the display name for the item.
     *
     * @param displayName The display name to set.
     * @return The instance of {@link ItemBuilder}.
     */
    public ItemBuilder setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    /**
     * Sets whether the item is unbreakable.
     *
     * @param unbreakable Whether the item is unbreakable.
     * @return The instance of {@link ItemBuilder}.
     */
    public ItemBuilder setUnbreakable(boolean unbreakable) {
        this.unbreakable = unbreakable;
        return this;
    }

    /**
     * Sets the lore for the item.
     *
     * @param lore The lore to set.
     * @return The instance of {@link ItemBuilder}.
     */
    public ItemBuilder setLore(List<String> lore) {
        this.lore = lore;
        return this;
    }

    /**
     * Adds an additional line to the lore for the item.
     *
     * @param lore The lore to add.
     * @return The instance of {@link ItemBuilder}.
     */
    public ItemBuilder addLore(String lore) {
        this.lore.add(lore);
        return this;
    }

    /**
     * Adds additional lines to the lore for the item.
     *
     * @param lore The lore to add.
     * @return The instance of {@link ItemBuilder}.
     */
    public ItemBuilder addLore(String... lore) {
        if(lore != null)
            Arrays.stream(lore).forEach(this::addLore);
        return this;
    }

    /**
     * Sets the enchantments for the item.
     *
     * @param enchantments The enchantments to set.
     * @return The instance of {@link ItemBuilder}.
     */
    public ItemBuilder setEnchantments(Map<Enchantment, Integer> enchantments) {
        this.enchantments = enchantments;
        return this;
    }

    /**
     * Adds an enchantment to the item.
     *
     * @param enchantment The enchantment to add.
     * @param level The level of the enchantment.
     * @return The instance of {@link ItemBuilder}.
     */
    public ItemBuilder addEnchantment(Enchantment enchantment, int level) {
        this.enchantments.put(enchantment, level);
        return this;
    }

    /**
     * Sets the item flags for the item.
     *
     * @param itemFlags The item flags to set.
     * @return The instance of {@link ItemBuilder}.
     */
    public ItemBuilder setItemFlags(List<ItemFlag> itemFlags) {
        this.itemFlags = itemFlags;
        return this;
    }

    /**
     * Adds an item flag to the item.
     *
     * @param itemFlag The item flag to add.
     * @return The instance of {@link ItemBuilder}.
     */
    public ItemBuilder addItemFlag(ItemFlag itemFlag) {
        this.itemFlags.add(itemFlag);
        return this;
    }

    /**
     * Adds multiple item flags to the item.
     *
     * @param itemFlags The item flags to add.
     * @return The instance of {@link ItemBuilder}.
     */
    public ItemBuilder addItemFlags(ItemFlag... itemFlags) {
        if(itemFlags != null)
            Arrays.stream(itemFlags).forEach(this::addItemFlag);
        return this;
    }

    /**
     * Sets the color for the item. This is mainly used if
     * {@link ItemType#LEATHER_ARMOUR} is set as the item type.
     *
     * @param color The color to set.
     * @return The instance of {@link ItemBuilder}.
     */
    public ItemBuilder setColor(Color color) {
        this.color = color;
        return this;
    }

    /**
     * Sets the main potion effect for the item. This is only used
     * if {@link ItemType#POTION} is set as the item type.
     *
     * @param mainEffect The main effect type to set.
     * @return The instance of {@link ItemBuilder}.
     */
    public ItemBuilder setMainEffect(PotionEffectType mainEffect) {
        this.mainEffect = mainEffect;
        return this;
    }

    /**
     * Sets the potion effects for the item. This is only used
     * if {@link ItemType#POTION} is set as the item type.
     *
     * @param potionEffects The potion effects to set.
     * @return The instance of {@link ItemBuilder}.
     */
    public ItemBuilder setPotionEffects(List<PotionEffect> potionEffects) {
        this.potionEffects = potionEffects;
        return this;
    }

    /**
     * Adds a potion effect to the item. This is only used if
     * {@link ItemType#POTION} is set as the item type.
     *
     * @param potionEffect The potion effect to add.
     * @return The instance of {@link ItemBuilder}.
     */
    public ItemBuilder addPotionEffect(PotionEffect potionEffect) {
        this.potionEffects.add(potionEffect);
        return this;
    }

    /**
     * Sets the skull owner for the item. This is only used if
     * {@link ItemType#SKULL} is set as the item type.
     *
     * @param skullOwner The skull owner to set.
     * @return The instance of {@link ItemBuilder}.
     */
    public ItemBuilder setSkullOwner(String skullOwner) {
        this.skullOwner = skullOwner;
        return this;
    }

    /**
     * Sets the metadata for the item.
     *
     * @param key The key of the metadata.
     * @param value The value of the metadata.
     * @return The instance of {@link ItemBuilder}.
     */
    public ItemBuilder addMetadata(String key, String value) {
        this.metadata.put(key, value);
        return this;
    }

    /**
     * Sets all listeners to the specified listeners.
     *
     * @param listeners The listeners to set.
     * @return The instance of {@link ItemBuilder}.
     */
    public ItemBuilder setListeners(Map<Action, Consumer<Player>> listeners) {
        this.listeners = listeners;
        return this;
    }

    /**
     * Adds a new listener to be called when a player clicks the
     * item with the specified action.
     *
     * @param action The type of action to invoke the call.
     * @param consumer The call to invoke.
     * @return The instance of {@link ItemBuilder}.
     */
    public ItemBuilder addListener(Action action, Consumer<Player> consumer) {
        this.listeners.put(action, consumer);
        return this;
    }

    /**
     * Adds a new listener to be called when a player clicks the
     * item, no matter what action is used. If another action is
     * already set, it will be skipped over.
     *
     * @param consumer The call to invoke.
     * @return The instance of {@link ItemBuilder}.
     */
    public ItemBuilder addListener(Consumer<Player> consumer) {
        Arrays.stream(Action.values()).forEach(action -> {
            if(this.listeners.getOrDefault(action, null) == null)
                this.listeners.put(action, consumer);
        });
        return this;
    }

    /**
     * Builds the item and sets the approriate values with
     * respect to the set {@link ItemType}.
     *
     * @return The built instance of {@link ItemStack}.
     */
    public ItemStack build() {
        ItemStack item = new ItemStack(material, amount, data);
        ItemMeta meta = item.getItemMeta();
        if(this.displayName != null)
            meta.setDisplayName(this.displayName);
        meta.setLore(this.lore);
        this.enchantments.forEach((ench, level) -> meta.addEnchant(ench, level, true));
        item.setItemMeta(meta);

        if(this.itemType == ItemType.LEATHER_ARMOUR) {
            LeatherArmorMeta meta2 = (LeatherArmorMeta) item.getItemMeta();
            if(this.color != null)
                meta2.setColor(this.color);
            item.setItemMeta(meta2);
        } else if(this.itemType == ItemType.POTION) {
            PotionMeta meta2 = (PotionMeta) item.getItemMeta();
            if(this.mainEffect != null)
                meta2.setMainEffect(this.mainEffect);
            this.potionEffects.forEach(effect -> meta2.addCustomEffect(effect, true));
            item.setItemMeta(meta2);
        } else if(this.itemType == ItemType.SKULL) {
            SkullMeta meta2 = (SkullMeta) item.getItemMeta();
            if(this.skullOwner != null) {
                meta2.setOwner(this.skullOwner);
                item.setDurability((short) 3);
            }
            item.setItemMeta(meta2);
        }

        if(metadata.size() >= 1) {
            List<MetaEntry> entries = Lists.newArrayList();
            for(Map.Entry<String, String> entry : metadata.entrySet()) {
                entries.add(new MetaEntry(entry.getKey(), entry.getValue()));
            }
            item = ItemStackUtil.setMetadata(item, entries.toArray(new MetaEntry[0]));
        }

        return item;
    }

    @Deprecated
    public static ItemBuilder newBuilder() {
        return new ItemBuilder();
    }

    @Deprecated
    public static ItemBuilder newBuilder(Material material, ItemType type) {
        return new ItemBuilder(material, type);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if(e.getItem() != null && e.getItem().hashCode() == build().hashCode()) {
            Consumer<Player> call = this.listeners.getOrDefault(e.getAction(), null);
            if(call != null)
                call.accept(e.getPlayer());
        }
    }

    @Override
    public ItemBuilder clone() {
        try {
            return (ItemBuilder) super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    /**
     * Defines the type of item to be built when using
     * {@link ItemBuilder}.
     */
    public enum ItemType {
         NORMAL,
         LEATHER_ARMOUR,
         POTION,
         SKULL
    }
}
